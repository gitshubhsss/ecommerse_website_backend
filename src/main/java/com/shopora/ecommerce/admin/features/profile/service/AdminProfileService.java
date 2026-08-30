package com.shopora.ecommerce.admin.features.profile.service;
import com.shopora.ecommerce.admin.features.auth.entities.Admin;
import com.shopora.ecommerce.admin.features.auth.repository.AdminRepository;
import com.shopora.ecommerce.admin.features.profile.dto.AdminProfileResponse;
import com.shopora.ecommerce.admin.features.profile.dto.AdminUpdateProfileRequest;
import com.shopora.ecommerce.common.exception.UnauthorizedException;
import com.shopora.ecommerce.config.JwtService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminProfileService {
    private final AdminRepository repository;

    private final JwtService jwtService;

    public AdminProfileResponse getAdminProfile(Long adminId) {
        Admin admin = repository.findById(adminId).orElseThrow(() -> new UnauthorizedException("Admin not found"));

        return AdminProfileResponse.builder(
        ).id(admin.getId()).email(admin.getEmail()).name(admin.getFullName()).isActive(admin.getIsActive()).build();

    }

    public AdminProfileResponse updateAdminProfile(AdminUpdateProfileRequest request) {

        Admin admin = repository.findById(request.getAdminId())
                .orElseThrow(() -> new UnauthorizedException("Admin not found"));

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            admin.setEmail(request.getEmail());
        }

        if (request.getName() != null && !request.getName().isBlank()) {
            admin.setFullName(request.getName());
        }

        if (request.getIsActive() != null) {
            admin.setIsActive(request.getIsActive());
        }

        Admin updatedAdmin = repository.save(admin);

        return AdminProfileResponse.builder()
                .id(updatedAdmin.getId())
                .email(updatedAdmin.getEmail())
                .name(updatedAdmin.getFullName())
                .isActive(updatedAdmin.getIsActive())
                .build();
    }


}
