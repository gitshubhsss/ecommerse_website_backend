package com.shopora.ecommerce.admin.features.auth.service;

import com.shopora.ecommerce.admin.features.auth.dto.AdminLoginRequest;
import com.shopora.ecommerce.admin.features.auth.dto.AdminLoginResponse;
import com.shopora.ecommerce.admin.features.auth.entities.Admin;
import com.shopora.ecommerce.admin.features.auth.repository.AdminRepository;
import com.shopora.ecommerce.common.exception.BadRequestException;
import com.shopora.ecommerce.common.exception.UnauthorizedException;
import com.shopora.ecommerce.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @org.springframework.beans.factory.annotation.Value("${jwt.admin-expiration-ms}")
    private long adminExpirationMs;

    public AdminLoginResponse login(AdminLoginRequest request) {

        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!Boolean.TRUE.equals(admin.getIsActive())) {
            throw new BadRequestException("Admin account is deactivated");
        }

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        // Generate real JWT token
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
}