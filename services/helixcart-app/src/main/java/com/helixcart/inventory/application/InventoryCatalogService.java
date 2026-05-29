package com.helixcart.inventory.application;

import com.helixcart.catalog.application.ProductCatalog;
import com.helixcart.common.exception.BusinessException;
import com.helixcart.common.exception.ResourceNotFoundException;
import com.helixcart.inventory.domain.InventoryItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Inventory application service.
 *
 * <p>This service coordinates product-aware inventory operations:
 * - Inventory can be created only for an existing product.
 * - Each product has one inventory record.
 * - Reservation quantity cannot exceed available stock.
 * - Release quantity cannot exceed reserved stock.
 *
 * <p>Catalog is accessed only through the public {@link ProductCatalog} port,
 * preserving module boundaries.
 */
@Service
public class InventoryCatalogService implements InventoryCatalog {

    private final InventoryRepository inventoryRepository;
    private final ProductCatalog productCatalog;

    public InventoryCatalogService(
            InventoryRepository inventoryRepository,
            ProductCatalog productCatalog) {
        this.inventoryRepository = inventoryRepository;
        this.productCatalog = productCatalog;
    }

    @Override
    @Transactional
    public InventoryItem create(InventoryCommand command) {
        ensureProductExists(command.productId());
        if (inventoryRepository.existsByProductId(command.productId())) {
            throw new BusinessException("INVENTORY_ALREADY_EXISTS", "Inventory already exists for product");
        }
        return inventoryRepository.save(command);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryItem getByProduct(UUID productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", productId.toString()));
    }

    @Override
    @Transactional
    public InventoryItem adjustStock(UUID productId, int quantityOnHand) {
        InventoryItem item = getByProduct(productId);
        return inventoryRepository.update(
                productId,
                quantityOnHand,
                item.quantityReserved(),
                item.reorderThreshold());
    }

    @Override
    @Transactional
    public InventoryItem reserve(UUID productId, int quantity) {
        if (quantity <= 0) {
            throw new BusinessException("INVALID_RESERVATION_QUANTITY", "Reservation quantity must be greater than zero");
        }
        InventoryItem item = getByProduct(productId);
        if (quantity > item.quantityAvailable()) {
            throw new BusinessException("INSUFFICIENT_INVENTORY", "Insufficient available inventory");
        }
        return inventoryRepository.update(
                productId,
                item.quantityOnHand(),
                item.quantityReserved() + quantity,
                item.reorderThreshold());
    }

    @Override
    @Transactional
    public InventoryItem release(UUID productId, int quantity) {
        if (quantity <= 0) {
            throw new BusinessException("INVALID_RELEASE_QUANTITY", "Release quantity must be greater than zero");
        }
        InventoryItem item = getByProduct(productId);
        if (quantity > item.quantityReserved()) {
            throw new BusinessException("RELEASE_EXCEEDS_RESERVED_QUANTITY", "Release quantity cannot exceed reserved quantity");
        }
        return inventoryRepository.update(
                productId,
                item.quantityOnHand(),
                item.quantityReserved() - quantity,
                item.reorderThreshold());
    }

    private void ensureProductExists(UUID productId) {
        productCatalog.get(productId);
    }
}
