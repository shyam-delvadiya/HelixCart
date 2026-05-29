package com.helixcart.inventory.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * HTTP request body for creating inventory for a product.
 */
@Schema(description = "Inventory create request")
public record InventoryRequest(
        @NotNull(message = "Product id is required")
        UUID productId,

        @Min(value = 0, message = "Quantity on hand must be zero or greater")
        int quantityOnHand,

        @Min(value = 0, message = "Reorder threshold must be zero or greater")
        int reorderThreshold
) {
}
