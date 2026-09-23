package com.shopora.ecommerce.admin.features.categories.controller;
import com.shopora.ecommerce.admin.features.categories.dto.CategoryResponse;
import com.shopora.ecommerce.admin.features.categories.dto.CreateCategoryRequest;
import com.shopora.ecommerce.admin.features.categories.service.CategoryService;
import com.shopora.ecommerce.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * NOTE: switched the base path to plural "/api/admin/categories" to match
 * the rest of your admin category endpoints (status, reorder, etc.).
 */
@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> addNewCategory(@Valid @RequestBody CreateCategoryRequest request) {
        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.ok(ApiResponse.success("Category added successfully",response));
    }
}
