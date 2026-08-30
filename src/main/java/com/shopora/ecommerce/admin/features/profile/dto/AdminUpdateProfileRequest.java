package com.shopora.ecommerce.admin.features.profile.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminUpdateProfileRequest {

    @NotNull(message = "Admin ID is required")
    private Long adminId;

    @Email(message = "Please provide a valid email")
    private String email;

    private String name;

    private Boolean isActive;
}