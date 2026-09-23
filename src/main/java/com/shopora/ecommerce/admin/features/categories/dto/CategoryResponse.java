package com.shopora.ecommerce.admin.features.categories.dto;

import com.shopora.ecommerce.admin.features.categories.enums.CategoryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    private String uuid;
    private String name;
    private String slug;
    private String description;
    private String imageUrl;
    private String iconUrl;
    private Integer level;
    private Integer displayOrder;
    private CategoryStatus status;
    private Boolean isFeatured;

    /** Uuid of the parent category, or null if this is a root-level category. */
    private String parentUuid;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
