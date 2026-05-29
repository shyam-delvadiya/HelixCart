package com.helixcart.inventory.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

/**
 * HTTP request body for reserving or releasing stock.
 *
 * <p>Reservations and releases must be positive deltas; setting absolute stock
 * uses {@link InventoryQuantityRequest}.
 */
@Schema(description = "Inventory reservation request")
public record InventoryReservationRequest(
        @Min(value = 1, message = "Quantity must be greater than zero")
        int quantity
) {
}
