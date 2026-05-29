package com.helixcart.inventory.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Inventory response returned to API consumers.
 *
 * <p>{@code quantityAvailable} and {@code belowReorderThreshold} are derived
 * values included to keep clients from duplicating inventory rules.
 */
@Schema(description = "Inventory response")
public record InventoryResponse(
        UUID id,
        UUID productId,
        int quantityOnHand,
        int quantityReserved,
        int quantityAvailable,
        int reorderThreshold,
        boolean belowReorderThreshold,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
