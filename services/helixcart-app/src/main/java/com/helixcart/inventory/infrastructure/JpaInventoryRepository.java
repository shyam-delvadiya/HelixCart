package com.helixcart.inventory.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data repository for inventory persistence.
 *
 * <p>Package-private by design; application code depends on
 * {@code InventoryRepository}, not this Spring Data interface.
 */
interface JpaInventoryRepository extends JpaRepository<InventoryEntity, UUID> {

    Optional<InventoryEntity> findByProductId(UUID productId);

    boolean existsByProductId(UUID productId);
}
