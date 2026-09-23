package com.shopora.ecommerce.admin.features.categories.entities;

import com.shopora.ecommerce.admin.features.categories.enums.CategoryAction;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "category_audit_log")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_audit_category"))
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false, length = 50)
    private CategoryAction action;

    /** JSON snapshot of the field(s) before the change. Null for CREATED actions. */
    @Column(name = "old_value", columnDefinition = "JSON")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "JSON")
    private String newValue;

    /** Admin user id who performed the action. */
    @Column(name = "performed_by")
    private Long performedBy;

    @CreationTimestamp
    @Column(name = "performed_at", nullable = false, updatable = false)
    private LocalDateTime performedAt;
}
