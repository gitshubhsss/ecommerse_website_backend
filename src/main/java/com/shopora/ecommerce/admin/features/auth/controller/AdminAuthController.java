package com.shopora.ecommerce.admin.features.auth.controller;


import com.shopora.ecommerce.admin.features.auth.dto.AdminLoginRequest;
import com.shopora.ecommerce.admin.features.auth.dto.AdminLoginResponse;
import com.shopora.ecommerce.admin.features.auth.service.AdminAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {
    private final AdminAuthService adminAuthService;

    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponse>login(@Valid @RequestBody AdminLoginRequest request){
        AdminLoginResponse response = adminAuthService.login(request);
        return ResponseEntity.ok(response);
    }
}
