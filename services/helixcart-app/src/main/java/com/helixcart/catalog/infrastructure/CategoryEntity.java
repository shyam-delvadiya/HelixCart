package com.helixcart.catalog.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * JPA mapping for the {@code categories} table.
 *
 * <p>The self-reference models the parent category relationship. Children are
 * intentionally not mapped here because current Phase 2 use cases only need
 * direct parent lookup and bounded category responses.
 */
@Entity
@Table(name = "categories")
class CategoryEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CategoryEntity parent;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    static CategoryEntity create(String name, String slug, String description, CategoryEntity parent) {
        CategoryEntity category = new CategoryEntity();
        category.id = UUID.randomUUID();
        category.apply(name, slug, description, parent);
        return category;
    }

    @PrePersist
    void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }
    }

    void apply(String name, String slug, String description, CategoryEntity parent) {
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.parent = parent;
    }

    UUID getId() {
        return id;
    }

    String getName() {
        return name;
    }

    String getSlug() {
        return slug;
    }

    String getDescription() {
        return description;
    }

    CategoryEntity getParent() {
        return parent;
    }

    OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
