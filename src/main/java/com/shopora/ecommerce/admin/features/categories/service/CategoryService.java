package com.shopora.ecommerce.admin.features.categories.service;

import com.shopora.ecommerce.admin.features.categories.dto.CategoryResponse;
import com.shopora.ecommerce.admin.features.categories.dto.CreateCategoryRequest;

public interface CategoryService {

    CategoryResponse createCategory(CreateCategoryRequest request);
}
