package com.shopora.ecommerce.admin.features.auth.controller;
import com.shopora.ecommerce.admin.features.auth.dto.*;
import com.shopora.ecommerce.admin.features.auth.service.AdminAuthService;
import com.shopora.ecommerce.common.dto.ApiResponse;
import com.shopora.ecommerce.config.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {
    private final AdminAuthService adminAuthService;
    private final JwtService jwtService;

    // Function to Log in
    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponse>login(@Valid @RequestBody AdminLoginRequest request){
        AdminLoginResponse response = adminAuthService.login(request);
        return ResponseEntity.ok(response);
    }

    // Function to Log out
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Object>>logout(HttpServletRequest request){
        adminAuthService.logout(request);
        return ResponseEntity.ok(ApiResponse.success("Logout successful"));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Object>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            HttpServletRequest httpRequest) {

        Long adminId = extractAdminId(httpRequest);
        adminAuthService.changePassword(adminId, request);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Object>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        adminAuthService.forgotPassword(request);
        return ResponseEntity.ok(ApiResponse.success("If that email exists, a reset link has been sent"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Object>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        adminAuthService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success("Password reset successful"));
    }

    private Long extractAdminId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new com.shopora.ecommerce.common.exception.UnauthorizedException("Invalid or missing token");
        }
        String token = authHeader.substring(7);
        return jwtService.extractAdminId(token); // see note below
    }
}
