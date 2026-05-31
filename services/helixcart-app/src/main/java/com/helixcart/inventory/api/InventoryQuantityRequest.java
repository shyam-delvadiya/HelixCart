package com.helixcart.inventory.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

/**
 * HTTP request body for setting stock on hand.
 *
 * <p>Zero is valid here because a product can be intentionally out of stock.
 */
@Schema(description = "Inventory quantity request")
public record InventoryQuantityRequest(
        @Min(value = 0, message = "Quantity must be zero or greater")
        int quantity
) {
}
