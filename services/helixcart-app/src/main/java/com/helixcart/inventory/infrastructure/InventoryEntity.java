package com.helixcart.inventory.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * JPA mapping for the {@code inventory} table.
 *
 * <p>The entity stores {@code product_id} as a UUID instead of mapping a JPA
 * relationship to Catalog's product entity. That keeps Inventory independent
 * from Catalog persistence internals while the database still protects the
 * foreign-key constraint.
 */
@Entity
@Table(name = "inventory")
class InventoryEntity {

    @Id
    private UUID id;

    @Column(name = "product_id", nullable = false, unique = true)
    private UUID productId;

    @Column(name = "quantity_on_hand", nullable = false)
    private int quantityOnHand;

    @Column(name = "quantity_reserved", nullable = false)
    private int quantityReserved;

    @Column(name = "reorder_threshold", nullable = false)
    private int reorderThreshold;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    static InventoryEntity create(UUID productId, int quantityOnHand, int reorderThreshold) {
        InventoryEntity entity = new InventoryEntity();
        entity.id = UUID.randomUUID();
        entity.productId = productId;
        entity.quantityOnHand = quantityOnHand;
        entity.quantityReserved = 0;
        entity.reorderThreshold = reorderThreshold;
        return entity;
    }

    @PrePersist
    void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }
    }

    void update(int quantityOnHand, int quantityReserved, int reorderThreshold) {
        this.quantityOnHand = quantityOnHand;
        this.quantityReserved = quantityReserved;
        this.reorderThreshold = reorderThreshold;
    }

    UUID getId() {
        return id;
    }

    UUID getProductId() {
        return productId;
    }

    int getQuantityOnHand() {
        return quantityOnHand;
    }

    int getQuantityReserved() {
        return quantityReserved;
    }

    int getReorderThreshold() {
        return reorderThreshold;
    }

    OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
