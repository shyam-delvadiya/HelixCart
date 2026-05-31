package com.helixcart.catalog.domain;

import java.util.UUID;

/**
 * Lightweight category projection used when another aggregate needs category
 * identity without owning the full category model.
 *
 * <p>Products expose attached categories through this summary to avoid leaking
 * persistence entities or coupling product responses to the full category tree.
 */
public record CategorySummary(
        UUID id,
        String name,
        String slug
) {
}
