package com.helixcart.inventory.application;

import com.helixcart.catalog.application.ProductCatalog;
import com.helixcart.catalog.domain.Product;
import com.helixcart.catalog.domain.ProductStatus;
import com.helixcart.common.exception.BusinessException;
import com.helixcart.inventory.domain.InventoryItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InventoryCatalogServiceTest {

    private final InMemoryInventoryRepository repository = new InMemoryInventoryRepository();
    private final FakeProductCatalog productCatalog = new FakeProductCatalog();
    private final InventoryCatalogService service = new InventoryCatalogService(repository, productCatalog);

    @Test
    void create_withExistingProduct_returnsInventory() {
        UUID productId = existingProduct();

        InventoryItem item = service.create(command(productId, 10, 2));

        assertThat(item.productId()).isEqualTo(productId);
        assertThat(item.quantityOnHand()).isEqualTo(10);
        assertThat(item.quantityAvailable()).isEqualTo(10);
    }

    @Test
    void create_withDuplicateProduct_throwsBusinessException() {
        UUID productId = existingProduct();
        service.create(command(productId, 10, 2));

        assertThatThrownBy(() -> service.create(command(productId, 5, 1)))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Inventory already exists for product");
    }

    @Test
    void reserve_withAvailableStock_increasesReservedQuantity() {
        UUID productId = existingProduct();
        service.create(command(productId, 10, 2));

        InventoryItem item = service.reserve(productId, 4);

        assertThat(item.quantityReserved()).isEqualTo(4);
        assertThat(item.quantityAvailable()).isEqualTo(6);
    }

    @Test
    void reserve_withInsufficientStock_throwsBusinessException() {
        UUID productId = existingProduct();
        service.create(command(productId, 3, 1));

        assertThatThrownBy(() -> service.reserve(productId, 4))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Insufficient available inventory");
    }

    @Test
    void release_withReservedStock_decreasesReservedQuantity() {
        UUID productId = existingProduct();
        service.create(command(productId, 10, 2));
        service.reserve(productId, 6);

        InventoryItem item = service.release(productId, 4);

        assertThat(item.quantityReserved()).isEqualTo(2);
        assertThat(item.quantityAvailable()).isEqualTo(8);
    }

    @Test
    void release_withQuantityGreaterThanReserved_throwsBusinessException() {
        UUID productId = existingProduct();
        service.create(command(productId, 10, 2));
        service.reserve(productId, 2);

        assertThatThrownBy(() -> service.release(productId, 3))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Release quantity cannot exceed reserved quantity");
    }

    @Test
    void adjustStock_belowReservedQuantity_throwsBusinessException() {
        UUID productId = existingProduct();
        service.create(command(productId, 10, 2));
        service.reserve(productId, 6);

        assertThatThrownBy(() -> service.adjustStock(productId, 5))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Reserved quantity cannot exceed stock on hand");
    }

    private UUID existingProduct() {
        UUID productId = UUID.randomUUID();
        productCatalog.add(new Product(
                productId,
                "SKU-" + productId,
                "Product",
                "Description",
                BigDecimal.TEN,
                ProductStatus.ACTIVE,
                Set.of(),
                OffsetDateTime.now(),
                OffsetDateTime.now()));
        return productId;
    }

    private InventoryCommand command(UUID productId, int quantityOnHand, int reorderThreshold) {
        return new InventoryCommand(productId, quantityOnHand, reorderThreshold);
    }

    private static class InMemoryInventoryRepository implements InventoryRepository {

        private final java.util.Map<UUID, InventoryItem> inventoryByProduct = new java.util.HashMap<>();

        @Override
        public InventoryItem save(InventoryCommand command) {
            InventoryItem item = new InventoryItem(
                    UUID.randomUUID(),
                    command.productId(),
                    command.quantityOnHand(),
                    0,
                    command.reorderThreshold(),
                    OffsetDateTime.now(),
                    OffsetDateTime.now());
            inventoryByProduct.put(command.productId(), item);
            return item;
        }

        @Override
        public Optional<InventoryItem> findByProductId(UUID productId) {
            return Optional.ofNullable(inventoryByProduct.get(productId));
        }

        @Override
        public boolean existsByProductId(UUID productId) {
            return inventoryByProduct.containsKey(productId);
        }

        @Override
        public InventoryItem update(UUID productId, int quantityOnHand, int quantityReserved, int reorderThreshold) {
            InventoryItem existing = inventoryByProduct.get(productId);
            InventoryItem item = new InventoryItem(
                    existing.id(),
                    existing.productId(),
                    quantityOnHand,
                    quantityReserved,
                    reorderThreshold,
                    existing.createdAt(),
                    OffsetDateTime.now());
            inventoryByProduct.put(productId, item);
            return item;
        }
    }

    private static class FakeProductCatalog implements ProductCatalog {

        private final java.util.Map<UUID, Product> products = new java.util.HashMap<>();

        void add(Product product) {
            products.put(product.id(), product);
        }

        @Override
        public Product create(com.helixcart.catalog.application.ProductCommand command) {
            throw new UnsupportedOperationException("Not needed for inventory service tests");
        }

        @Override
        public Product update(UUID productId, com.helixcart.catalog.application.ProductCommand command) {
            throw new UnsupportedOperationException("Not needed for inventory service tests");
        }

        @Override
        public Product updateStatus(UUID productId, ProductStatus status) {
            throw new UnsupportedOperationException("Not needed for inventory service tests");
        }

        @Override
        public Product get(UUID productId) {
            return Optional.ofNullable(products.get(productId))
                    .orElseThrow(() -> new com.helixcart.common.exception.ResourceNotFoundException(
                            "Product",
                            productId.toString()));
        }

        @Override
        public Optional<Product> findBySku(String sku) {
            throw new UnsupportedOperationException("Not needed for inventory service tests");
        }

        @Override
        public org.springframework.data.domain.Page<Product> search(
                String query,
                ProductStatus status,
                org.springframework.data.domain.Pageable pageable) {
            throw new UnsupportedOperationException("Not needed for inventory service tests");
        }
    }
}
