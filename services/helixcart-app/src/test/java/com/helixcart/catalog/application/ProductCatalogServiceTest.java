package com.helixcart.catalog.application;

import com.helixcart.catalog.domain.Product;
import com.helixcart.catalog.domain.ProductStatus;
import com.helixcart.common.exception.BusinessException;
import com.helixcart.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductCatalogServiceTest {

    private final InMemoryProductRepository repository = new InMemoryProductRepository();
    private final ProductCatalogService service = new ProductCatalogService(repository);

    @Test
    void create_withValidRequest_returnsCreatedProduct() {
        ProductCommand command = command("SKU-1", Set.of());

        Product product = service.create(command);

        assertThat(product.id()).isNotNull();
        assertThat(product.sku()).isEqualTo("SKU-1");
        assertThat(product.status()).isEqualTo(ProductStatus.ACTIVE);
    }

    @Test
    void create_withDuplicateSku_throwsBusinessException() {
        service.create(command("SKU-1", Set.of()));

        assertThatThrownBy(() -> service.create(command("sku-1", Set.of())))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Product SKU already exists");
    }

    @Test
    void create_withMissingCategory_throwsBusinessException() {
        UUID missingCategoryId = UUID.randomUUID();

        assertThatThrownBy(() -> service.create(command("SKU-1", Set.of(missingCategoryId))))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("One or more product categories do not exist");
    }

    @Test
    void update_withUnknownProduct_throwsResourceNotFoundException() {
        UUID productId = UUID.randomUUID();

        assertThatThrownBy(() -> service.update(productId, command("SKU-1", Set.of())))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Product not found with id: " + productId);
    }

    @Test
    void updateStatus_withExistingProduct_updatesStatus() {
        Product product = service.create(command("SKU-1", Set.of()));

        Product updated = service.updateStatus(product.id(), ProductStatus.INACTIVE);

        assertThat(updated.status()).isEqualTo(ProductStatus.INACTIVE);
    }

    private ProductCommand command(String sku, Set<UUID> categoryIds) {
        return new ProductCommand(
                sku,
                "Wireless Keyboard",
                "Low-profile keyboard",
                BigDecimal.valueOf(89.99),
                ProductStatus.ACTIVE,
                categoryIds);
    }

    private static class InMemoryProductRepository implements ProductRepository {

        private final java.util.Map<UUID, Product> products = new java.util.HashMap<>();

        @Override
        public Product save(ProductCommand command) {
            Product product = new Product(
                    UUID.randomUUID(),
                    command.sku(),
                    command.name(),
                    command.description(),
                    command.price(),
                    command.status(),
                    Set.of(),
                    OffsetDateTime.now(),
                    OffsetDateTime.now());
            products.put(product.id(), product);
            return product;
        }

        @Override
        public Product update(UUID productId, ProductCommand command) {
            Product product = new Product(
                    productId,
                    command.sku(),
                    command.name(),
                    command.description(),
                    command.price(),
                    command.status(),
                    Set.of(),
                    OffsetDateTime.now(),
                    OffsetDateTime.now());
            products.put(productId, product);
            return product;
        }

        @Override
        public Product updateStatus(UUID productId, ProductStatus status) {
            Product existing = products.get(productId);
            Product product = new Product(
                    existing.id(),
                    existing.sku(),
                    existing.name(),
                    existing.description(),
                    existing.price(),
                    status,
                    existing.categories(),
                    existing.createdAt(),
                    OffsetDateTime.now());
            products.put(productId, product);
            return product;
        }

        @Override
        public Optional<Product> findById(UUID productId) {
            return Optional.ofNullable(products.get(productId));
        }

        @Override
        public Optional<Product> findBySku(String sku) {
            return products.values().stream()
                    .filter(product -> product.sku().equalsIgnoreCase(sku))
                    .findFirst();
        }

        @Override
        public boolean existsBySku(String sku) {
            return findBySku(sku).isPresent();
        }

        @Override
        public boolean existsBySkuAndIdNot(String sku, UUID productId) {
            return products.values().stream()
                    .anyMatch(product -> product.sku().equalsIgnoreCase(sku) && !product.id().equals(productId));
        }

        @Override
        public Page<Product> search(String query, ProductStatus status, Pageable pageable) {
            return Page.empty(pageable);
        }

        @Override
        public Set<UUID> findExistingCategoryIds(Set<UUID> categoryIds) {
            return Set.of();
        }
    }
}
