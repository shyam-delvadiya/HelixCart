package com.helixcart.inventory.application;

import java.util.UUID;

/**
 * Application-layer command for creating an inventory record.
 */
public record InventoryCommand(
        UUID productId,
        int quantityOnHand,
        int reorderThreshold
) {
}
