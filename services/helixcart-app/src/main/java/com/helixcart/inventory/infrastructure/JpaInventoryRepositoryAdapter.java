package com.helixcart.inventory.infrastructure;

import com.helixcart.common.exception.ResourceNotFoundException;
import com.helixcart.inventory.application.InventoryCommand;
import com.helixcart.inventory.application.InventoryRepository;
import com.helixcart.inventory.domain.InventoryItem;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA implementation of the inventory persistence port.
 *
 * <p>The adapter translates database rows into {@link InventoryItem}, allowing
 * the domain constructor to enforce stock invariants even after data is read
 * from persistence.
 */
@Repository
class JpaInventoryRepositoryAdapter implements InventoryRepository {

    private final JpaInventoryRepository inventoryRepository;

    JpaInventoryRepositoryAdapter(JpaInventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public InventoryItem save(InventoryCommand command) {
        InventoryEntity entity = InventoryEntity.create(
                command.productId(),
                command.quantityOnHand(),
                command.reorderThreshold());
        return toDomain(inventoryRepository.save(entity));
    }

    @Override
    public Optional<InventoryItem> findByProductId(UUID productId) {
        return inventoryRepository.findByProductId(productId).map(this::toDomain);
    }

    @Override
    public boolean existsByProductId(UUID productId) {
        return inventoryRepository.existsByProductId(productId);
    }

    @Override
    public InventoryItem update(UUID productId, int quantityOnHand, int quantityReserved, int reorderThreshold) {
        InventoryEntity entity = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", productId.toString()));
        entity.update(quantityOnHand, quantityReserved, reorderThreshold);
        return toDomain(inventoryRepository.save(entity));
    }

    private InventoryItem toDomain(InventoryEntity entity) {
        return new InventoryItem(
                entity.getId(),
                entity.getProductId(),
                entity.getQuantityOnHand(),
                entity.getQuantityReserved(),
                entity.getReorderThreshold(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
