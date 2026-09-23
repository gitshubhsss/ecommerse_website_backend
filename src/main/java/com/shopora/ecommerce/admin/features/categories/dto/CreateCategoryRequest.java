package com.shopora.ecommerce.admin.features.categories.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request payload for creating a new category.
 * Used by POST /api/admin/categories.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCategoryRequest {

    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Category name must not exceed 100 characters")
    private String name;

    /** Optional. If blank, the service generates a slug from {@link #name}. */
    @Size(max = 150, message = "Slug must not exceed 150 characters")
    private String slug;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @Size(max = 500)
    private String imageUrl;

    @Size(max = 500)
    private String iconUrl;

    /**
     * UUID (public identifier) of the parent category.
     * Null/blank means this is a root-level category.
     * We accept uuid here, never the internal numeric id, to keep the API
     * consistent with how other (future) services will reference categories.
     */
    private String parentUuid;

    @PositiveOrZero(message = "Display order cannot be negative")
    private Integer displayOrder;

    private Boolean isFeatured;
}
