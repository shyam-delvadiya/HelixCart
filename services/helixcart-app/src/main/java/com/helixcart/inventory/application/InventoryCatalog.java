package com.helixcart.inventory.application;

import com.helixcart.inventory.domain.InventoryItem;

import java.util.UUID;

/**
 * Public application port for inventory use cases.
 *
 * <p>Order workflows will use this port later to reserve and release stock
 * without depending on inventory persistence internals.
 */
public interface InventoryCatalog {

    InventoryItem create(InventoryCommand command);

    InventoryItem getByProduct(UUID productId);

    InventoryItem adjustStock(UUID productId, int quantityOnHand);

    InventoryItem reserve(UUID productId, int quantity);

    InventoryItem release(UUID productId, int quantity);
}
