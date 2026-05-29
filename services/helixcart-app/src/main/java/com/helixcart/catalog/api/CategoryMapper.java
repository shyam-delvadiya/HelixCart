package com.helixcart.catalog.api;

import com.helixcart.catalog.application.CategoryCommand;
import com.helixcart.catalog.domain.Category;

/**
 * Maps between category API DTOs and application/domain objects.
 */
class CategoryMapper {

    private CategoryMapper() {
    }

    static CategoryCommand toCommand(CategoryRequest request) {
        return new CategoryCommand(
                request.name(),
                request.slug(),
                request.description(),
                request.parentId());
    }

    static CategoryResponse toResponse(Category category) {
        CategorySummaryResponse parent = category.parent() == null
                ? null
                : new CategorySummaryResponse(
                        category.parent().id(),
                        category.parent().name(),
                        category.parent().slug());

        return new CategoryResponse(
                category.id(),
                category.name(),
                category.slug(),
                category.description(),
                parent,
                category.createdAt(),
                category.updatedAt());
    }
}
