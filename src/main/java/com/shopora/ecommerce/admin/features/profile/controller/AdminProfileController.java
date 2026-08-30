package com.shopora.ecommerce.admin.features.profile.controller;

import com.shopora.ecommerce.admin.features.profile.dto.AdminProfileResponse;
import com.shopora.ecommerce.admin.features.profile.dto.AdminUpdateProfileRequest;
import com.shopora.ecommerce.admin.features.profile.service.AdminProfileService;
import com.shopora.ecommerce.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/profile")
@RequiredArgsConstructor
public class AdminProfileController {

    private final AdminProfileService adminProfileService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AdminProfileResponse>> getAdminProfile(
            @AuthenticationPrincipal Long adminId) {

        AdminProfileResponse profile = adminProfileService.getAdminProfile(adminId);
        return ResponseEntity.ok(ApiResponse.success("Profile get successfully", profile));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<AdminProfileResponse>> updateAdminProfile(
            @Valid @RequestBody AdminUpdateProfileRequest request
    ) {
        AdminProfileResponse profile = adminProfileService.updateAdminProfile(request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", profile));
    }
}