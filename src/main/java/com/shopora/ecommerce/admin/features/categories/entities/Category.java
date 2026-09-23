package com.shopora.ecommerce.admin.features.categories.entities;

import com.shopora.ecommerce.admin.features.categories.enums.CategoryStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(
        name = "categories",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_category_uuid", columnNames = "uuid"),
                @UniqueConstraint(name = "uq_category_slug_parent", columnNames = {"slug", "parent_id"})
        },
        indexes = {
                @Index(name = "idx_category_parent", columnList = "parent_id"),
                @Index(name = "idx_category_status", columnList = "status"),
                @Index(name = "idx_category_parent_order", columnList = "parent_id, display_order"),
                @Index(name = "idx_category_deleted_at", columnList = "deleted_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid", nullable = false, updatable = false, length = 36)
    private String uuid;

    // Parent category null means this is root-level category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", foreignKey = @ForeignKey(name = "fk_category_parent"))
    private Category parent;


    @OneToMany(mappedBy = "parent", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Category> children = new ArrayList<>();

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "slug", nullable = false, length = 150)
    private String slug;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "icon_url", length = 500)
    private String iconUrl;

    @Column(name = "level", nullable = false)
    @Builder.Default
    private Integer level = 0;

    @Column(name = "display_order", nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;


    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private CategoryStatus status = CategoryStatus.ACTIVE;

    @Column(name = "is_featured", nullable = false)
    @Builder.Default
    private Boolean isFeatured = false;

    /**
     * Admin user id who created this row. Kept as a plain id (not a @ManyToOne to a User entity)
     * to avoid a hard cross-module dependency on the User/Admin feature.
     */
    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Soft-delete marker. Null = active row. Never hard-delete a category that products may reference.
     */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        }
    }

    /**
     * Convenience check so service/repository code doesn't compare deletedAt directly everywhere.
     */
    @Transient
    public boolean isDeleted() {
        return this.deletedAt != null;
    }

}
