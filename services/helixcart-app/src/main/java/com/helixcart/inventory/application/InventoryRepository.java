package com.helixcart.inventory.application;

import com.helixcart.inventory.domain.InventoryItem;

import java.util.Optional;
import java.util.UUID;

/**
 * Persistence port required by inventory use cases.
 *
 * <p>The application layer owns this interface so stock rules can be tested
 * without a database and implemented by JPA in the infrastructure layer.
 */
public interface InventoryRepository {

    InventoryItem save(InventoryCommand command);

    Optional<InventoryItem> findByProductId(UUID productId);

    boolean existsByProductId(UUID productId);

    InventoryItem update(UUID productId, int quantityOnHand, int quantityReserved, int reorderThreshold);
}
