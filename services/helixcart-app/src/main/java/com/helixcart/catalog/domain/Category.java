package com.helixcart.catalog.domain;

import com.helixcart.common.exception.BusinessException;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Catalog category domain model.
 *
 * <p>Categories group products and may form a parent-child hierarchy. The model
 * stores only a parent summary instead of a recursive child collection so API
 * responses remain bounded and the domain object stays simple.
 *
 * <p>Current Phase 2 invariants:
 * - Name is required.
 * - Slug is required and normalized to lowercase.
 */
public class Category {

    private final UUID id;
    private final String name;
    private final String slug;
    private final String description;
    private final CategorySummary parent;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;

    public Category(
            UUID id,
            String name,
            String slug,
            String description,
            CategorySummary parent,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt) {
        if (name == null || name.isBlank()) {
            throw new BusinessException("INVALID_CATEGORY_NAME", "Category name is required");
        }
        if (slug == null || slug.isBlank()) {
            throw new BusinessException("INVALID_CATEGORY_SLUG", "Category slug is required");
        }

        this.id = id;
        this.name = name.trim();
        this.slug = normalizeSlug(slug);
        this.description = description;
        this.parent = parent;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String slug() {
        return slug;
    }

    public String description() {
        return description;
    }

    public CategorySummary parent() {
        return parent;
    }

    public OffsetDateTime createdAt() {
        return createdAt;
    }

    public OffsetDateTime updatedAt() {
        return updatedAt;
    }

    public static String normalizeSlug(String slug) {
        return slug.trim().toLowerCase();
    }
}
