package com.helixcart.inventory.domain;

import com.helixcart.common.exception.BusinessException;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Inventory domain model for a single product.
 *
 * <p>The inventory module owns stock quantities and reservation state. Other
 * modules should ask Inventory for availability instead of calculating it from
 * product data.
 *
 * <p>Current Phase 2 invariants:
 * - Product id is required.
 * - Quantities cannot be negative.
 * - Reserved quantity cannot exceed stock on hand.
 * - Reorder threshold cannot be negative.
 */
public class InventoryItem {

    private final UUID id;
    private final UUID productId;
    private final int quantityOnHand;
    private final int quantityReserved;
    private final int reorderThreshold;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;

    public InventoryItem(
            UUID id,
            UUID productId,
            int quantityOnHand,
            int quantityReserved,
            int reorderThreshold,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt) {
        if (productId == null) {
            throw new BusinessException("INVALID_INVENTORY_PRODUCT", "Product id is required");
        }
        if (quantityOnHand < 0) {
            throw new BusinessException("INVALID_INVENTORY_QUANTITY", "Quantity on hand must be zero or greater");
        }
        if (quantityReserved < 0) {
            throw new BusinessException("INVALID_RESERVED_QUANTITY", "Reserved quantity must be zero or greater");
        }
        if (quantityReserved > quantityOnHand) {
            throw new BusinessException("RESERVED_QUANTITY_EXCEEDS_STOCK", "Reserved quantity cannot exceed stock on hand");
        }
        if (reorderThreshold < 0) {
            throw new BusinessException("INVALID_REORDER_THRESHOLD", "Reorder threshold must be zero or greater");
        }

        this.id = id;
        this.productId = productId;
        this.quantityOnHand = quantityOnHand;
        this.quantityReserved = quantityReserved;
        this.reorderThreshold = reorderThreshold;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID id() {
        return id;
    }

    public UUID productId() {
        return productId;
    }

    public int quantityOnHand() {
        return quantityOnHand;
    }

    public int quantityReserved() {
        return quantityReserved;
    }

    public int quantityAvailable() {
        return quantityOnHand - quantityReserved;
    }

    public int reorderThreshold() {
        return reorderThreshold;
    }

    public boolean belowReorderThreshold() {
        return quantityAvailable() <= reorderThreshold;
    }

    public OffsetDateTime createdAt() {
        return createdAt;
    }

    public OffsetDateTime updatedAt() {
        return updatedAt;
    }
}
