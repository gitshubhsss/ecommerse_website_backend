package com.shopora.ecommerce.admin.features.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminLoginResponse {

    private String token;
    private String tokenType;
    private Long expiresIn;     // milliseconds

    private AdminInfo user;

    @Getter
    @Builder
    public static class AdminInfo {
        private Long id;
        private String name;
        private String email;
    }
}