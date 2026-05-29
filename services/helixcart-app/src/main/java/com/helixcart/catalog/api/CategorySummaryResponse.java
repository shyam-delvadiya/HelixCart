package com.helixcart.catalog.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

/**
 * Category summary used inside other API responses.
 */
@Schema(description = "Category attached to a product")
public record CategorySummaryResponse(
        UUID id,
        String name,
        String slug
) {
}
