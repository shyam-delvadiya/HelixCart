package com.helixcart.catalog.application;

import com.helixcart.catalog.domain.Product;
import com.helixcart.catalog.domain.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Persistence port required by the product catalog use case.
 *
 * <p>The application layer owns this interface. Infrastructure adapters
 * implement it with JPA, Redis, or another persistence mechanism without
 * changing product business logic.
 */
public interface ProductRepository {

    Product save(ProductCommand command);

    Product update(UUID productId, ProductCommand command);

    Product updateStatus(UUID productId, ProductStatus status);

    Optional<Product> findById(UUID productId);

    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

    boolean existsBySkuAndIdNot(String sku, UUID productId);

    Page<Product> search(String query, ProductStatus status, Pageable pageable);

    Set<UUID> findExistingCategoryIds(Set<UUID> categoryIds);
}
