package com.helixcart.catalog.api;

import com.helixcart.catalog.application.ProductCommand;
import com.helixcart.catalog.domain.Product;

import java.util.stream.Collectors;

/**
 * Maps between product API DTOs and application/domain objects.
 *
 * <p>Keeping mapping code explicit avoids framework magic and makes API shape
 * changes easy to review.
 */
class ProductMapper {

    private ProductMapper() {
    }

    static ProductCommand toCommand(ProductRequest request) {
        return new ProductCommand(
                request.sku(),
                request.name(),
                request.description(),
                request.price(),
                request.status(),
                request.categoryIds());
    }

    static ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.id(),
                product.sku(),
                product.name(),
                product.description(),
                product.price(),
                product.status(),
                product.categories().stream()
                        .map(category -> new CategorySummaryResponse(
                                category.id(),
                                category.name(),
                                category.slug()))
                        .collect(Collectors.toSet()),
                product.createdAt(),
                product.updatedAt());
    }
}
