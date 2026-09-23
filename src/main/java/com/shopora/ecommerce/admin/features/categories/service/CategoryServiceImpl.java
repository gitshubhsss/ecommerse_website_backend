package com.shopora.ecommerce.admin.features.categories.service;

import com.shopora.ecommerce.admin.features.categories.dto.CategoryResponse;
import com.shopora.ecommerce.admin.features.categories.dto.CreateCategoryRequest;
import com.shopora.ecommerce.admin.features.categories.entities.Category;
import com.shopora.ecommerce.admin.features.categories.enums.CategoryStatus;
import com.shopora.ecommerce.admin.features.categories.repository.CategoryRepository;
import com.shopora.ecommerce.admin.features.categories.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    //if any operation throws a runtime exception, all changes are automatically rolled back, preventing partial data updates and corruption
    public CategoryResponse createCategory(CreateCategoryRequest request) {

        // Initially the category will be null
        Category parent = null;
        int level = 0;

        // Resolve parent (if provided) via its public uuid — never trust an internal id from a client.
        if (StringUtils.hasText(request.getParentUuid())) {
            parent = categoryRepository.findByUuidAndDeletedAtIsNull(request.getParentUuid())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Parent category not found with uuid: " + request.getParentUuid()));
            level = parent.getLevel() + 1;
        }

        String slug = StringUtils.hasText(request.getSlug())
                ? slugify(request.getSlug())
                : slugify(request.getName());

        boolean slugAlreadyExists = (parent == null)
                ? categoryRepository.existsBySlugAndParentIsNullAndDeletedAtIsNull(slug)
                : categoryRepository.existsBySlugAndParent_UuidAndDeletedAtIsNull(slug, parent.getUuid());

        if (slugAlreadyExists) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A category with slug '" + slug + "' already exists under this parent");
        }

        Category category = Category.builder()
                .name(request.getName())
                .slug(slug)
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .iconUrl(request.getIconUrl())
                .parent(parent)
                .level(level)
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .isFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : false)
                .status(CategoryStatus.ACTIVE)
                .build();

        Category saved = categoryRepository.save(category);
        log.info("Created category '{}' (uuid={}, parentUuid={})",
                saved.getName(), saved.getUuid(), parent != null ? parent.getUuid() : null);

        return toResponse(saved);
    }

    private CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .uuid(category.getUuid())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .iconUrl(category.getIconUrl())
                .level(category.getLevel())
                .displayOrder(category.getDisplayOrder())
                .status(category.getStatus())
                .isFeatured(category.getIsFeatured())
                .parentUuid(category.getParent() != null ? category.getParent().getUuid() : null)
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }

    /**
     * Converts a name into a URL-friendly slug: lowercase, non-alphanumeric characters
     * stripped, whitespace collapsed to single hyphens.
     * e.g. "Dairy & Breakfast" -> "dairy-breakfast"
     */
    private String slugify(String input) {
        return input.trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
    }
}
