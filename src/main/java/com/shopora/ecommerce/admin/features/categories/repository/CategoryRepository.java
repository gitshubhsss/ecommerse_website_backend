package com.shopora.ecommerce.admin.features.categories.repository;

import com.shopora.ecommerce.admin.features.categories.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    /** Fetch a non-deleted category by its public uuid. Use this for all external lookups. */
    Optional<Category> findByUuidAndDeletedAtIsNull(String uuid);

    /** Slug-uniqueness check for a root-level category (no parent). */
    boolean existsBySlugAndParentIsNullAndDeletedAtIsNull(String slug);

    /** Slug-uniqueness check scoped to a specific parent, matching the DB's uq_category_slug_parent constraint. */
    boolean existsBySlugAndParent_UuidAndDeletedAtIsNull(String slug, String parentUuid);
}
