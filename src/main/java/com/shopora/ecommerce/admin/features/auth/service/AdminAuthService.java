package com.shopora.ecommerce.admin.features.auth.service;

import com.shopora.ecommerce.admin.features.auth.dto.*;
import com.shopora.ecommerce.admin.features.auth.entities.Admin;
import com.shopora.ecommerce.admin.features.auth.repository.AdminRepository;
import com.shopora.ecommerce.common.exception.BadRequestException;
import com.shopora.ecommerce.common.exception.UnauthorizedException;
import com.shopora.ecommerce.common.service.EmailService;
import com.shopora.ecommerce.config.JwtService;
import com.shopora.ecommerce.config.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;
    private final EmailService emailService;

    @Value("${jwt.admin-expiration-ms}")
    private long adminExpirationMs;

    @Value("${app.frontend.reset-password-url}")
    private String resetPasswordUrl;

    private static final long RESET_TOKEN_VALID_MINUTES = 30;

    public AdminLoginResponse login(AdminLoginRequest request) {
        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!Boolean.TRUE.equals(admin.getIsActive())) {
            throw new BadRequestException("Admin account is deactivated");
        }

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        String token = jwtService.generateAdminToken(admin.getId(), admin.getEmail());

        return AdminLoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(adminExpirationMs)
                .user(AdminLoginResponse.AdminInfo.builder()
                        .id(admin.getId())
                        .name(admin.getFullName())
                        .email(admin.getEmail())
                        .build())
                .build();
    }

    public void logout(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Invalid or missing token");
        }
        String token = authHeader.substring(7);
        tokenBlacklistService.blacklistToken(token);
    }

    /**
     * Change password for the currently logged-in admin.
     * adminId comes from the JWT (set by your security filter), NOT from the request body.
     */
    public void changePassword(Long adminId, ChangePasswordRequest request) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new UnauthorizedException("Admin not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), admin.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("New password and confirm password do not match");
        }

        if (passwordEncoder.matches(request.getNewPassword(), admin.getPassword())) {
            throw new BadRequestException("New password must be different from current password");
        }

        admin.setPassword(passwordEncoder.encode(request.getNewPassword()));
        adminRepository.save(admin);
    }

    /**
     * Generates a reset token and emails a reset link.
     * Always responds the same way whether or not the email exists, to avoid leaking
     * which emails are registered admins.
     */
    public void forgotPassword(ForgotPasswordRequest request) {
        adminRepository.findByEmail(request.getEmail()).ifPresent(admin -> {
            String token = UUID.randomUUID().toString();
            admin.setResetPasswordToken(token);
            admin.setResetPasswordTokenExpiry(LocalDateTime.now().plusMinutes(RESET_TOKEN_VALID_MINUTES));
            adminRepository.save(admin);

            String link = resetPasswordUrl + "?token=" + token;
            emailService.sendPasswordResetEmail(admin.getEmail(), link);
        });
    }

    // Function to reset the password
    public void resetPassword(ResetPasswordRequest request) {
        Admin admin = adminRepository.findByResetPasswordToken(request.getToken())
                .orElseThrow(() -> new BadRequestException("Invalid or expired reset token"));

        if (admin.getResetPasswordTokenExpiry() == null
                || admin.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Reset token has expired. Please request a new one.");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("New password and confirm password do not match");
        }

        admin.setPassword(passwordEncoder.encode(request.getNewPassword()));
        admin.setResetPasswordToken(null);
        admin.setResetPasswordTokenExpiry(null);
        adminRepository.save(admin);
    }
}