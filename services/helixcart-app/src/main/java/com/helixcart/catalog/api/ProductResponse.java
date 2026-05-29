package com.helixcart.catalog.api;

import com.helixcart.catalog.domain.ProductStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * Product payload returned by Catalog APIs.
 *
 * <p>Categories are embedded as summaries so callers can display product
 * classification without loading the full category hierarchy.
 */
@Schema(description = "Product response")
public record ProductResponse(
        UUID id,
        String sku,
        String name,
        String description,
        BigDecimal price,
        ProductStatus status,
        Set<CategorySummaryResponse> categories,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
