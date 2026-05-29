package com.helixcart.inventory.api;

import com.helixcart.inventory.application.InventoryCommand;
import com.helixcart.inventory.domain.InventoryItem;

/**
 * Maps between inventory API DTOs and application/domain objects.
 */
class InventoryMapper {

    private InventoryMapper() {
    }

    static InventoryCommand toCommand(InventoryRequest request) {
        return new InventoryCommand(
                request.productId(),
                request.quantityOnHand(),
                request.reorderThreshold());
    }

    static InventoryResponse toResponse(InventoryItem item) {
        return new InventoryResponse(
                item.id(),
                item.productId(),
                item.quantityOnHand(),
                item.quantityReserved(),
                item.quantityAvailable(),
                item.reorderThreshold(),
                item.belowReorderThreshold(),
                item.createdAt(),
                item.updatedAt());
    }
}
