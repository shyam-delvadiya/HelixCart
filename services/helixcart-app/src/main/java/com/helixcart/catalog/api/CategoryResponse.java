package com.helixcart.catalog.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Category payload returned by Catalog APIs.
 *
 * <p>The parent field is a summary to keep hierarchy responses bounded and easy
 * to consume.
 */
@Schema(description = "Category response")
public record CategoryResponse(
        UUID id,
        String name,
        String slug,
        String description,
        CategorySummaryResponse parent,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
