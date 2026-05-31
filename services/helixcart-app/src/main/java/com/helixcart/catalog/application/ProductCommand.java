package com.helixcart.catalog.application;

import com.helixcart.catalog.domain.ProductStatus;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

/**
 * Application-layer command for creating or replacing a product.
 *
 * <p>The command is shared by API and application code, but it is not an HTTP
 * DTO. Controllers translate request DTOs into this command before invoking the
 * use case.
 */
public record ProductCommand(
        String sku,
        String name,
        String description,
        BigDecimal price,
        ProductStatus status,
        Set<UUID> categoryIds
) {
}
