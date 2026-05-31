package com.helixcart.catalog.application;

import com.helixcart.catalog.domain.Product;
import com.helixcart.catalog.domain.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Public application port for product catalog use cases.
 *
 * <p>Other modules may depend on this interface when they need product
 * information. They must not reach into catalog persistence entities or
 * repositories directly.
 */
public interface ProductCatalog {

    Product create(ProductCommand command);

    Product update(UUID productId, ProductCommand command);

    Product updateStatus(UUID productId, ProductStatus status);

    Product get(UUID productId);

    Optional<Product> findBySku(String sku);

    Page<Product> search(String query, ProductStatus status, Pageable pageable);
}
