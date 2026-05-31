package com.helixcart.catalog.application;

import java.util.UUID;

/**
 * Application-layer command for creating or replacing a category.
 *
 * <p>Using a command object keeps the use case API stable even if the external
 * HTTP request shape changes later.
 */
public record CategoryCommand(
        String name,
        String slug,
        String description,
        UUID parentId
) {
}
