package com.helixcart.catalog.application;

import com.helixcart.catalog.domain.Product;
import com.helixcart.catalog.domain.ProductStatus;
import com.helixcart.common.exception.BusinessException;
import com.helixcart.common.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Product catalog application service.
 *
 * <p>This service coordinates product use cases and enforces rules that need
 * repository access:
 * - Product SKUs must be unique.
 * - Referenced category IDs must exist before a product is saved.
 * - Updates and status changes require an existing product.
 *
 * <p>Domain-only validation remains in {@code Product}; persistence and query
 * details remain behind {@link ProductRepository}.
 */
@Service
public class ProductCatalogService implements ProductCatalog {

    private final ProductRepository productRepository;

    public ProductCatalogService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Product create(ProductCommand command) {
        validateCategories(command.categoryIds());
        if (productRepository.existsBySku(command.sku())) {
            throw new BusinessException("PRODUCT_SKU_ALREADY_EXISTS", "Product SKU already exists");
        }
        return productRepository.save(command);
    }

    @Override
    @Transactional
    public Product update(UUID productId, ProductCommand command) {
        ensureProductExists(productId);
        validateCategories(command.categoryIds());
        if (productRepository.existsBySkuAndIdNot(command.sku(), productId)) {
            throw new BusinessException("PRODUCT_SKU_ALREADY_EXISTS", "Product SKU already exists");
        }
        return productRepository.update(productId, command);
    }

    @Override
    @Transactional
    public Product updateStatus(UUID productId, ProductStatus status) {
        ensureProductExists(productId);
        return productRepository.updateStatus(productId, status);
    }

    @Override
    @Transactional(readOnly = true)
    public Product get(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> search(String query, ProductStatus status, Pageable pageable) {
        return productRepository.search(query, status, pageable);
    }

    private void ensureProductExists(UUID productId) {
        if (productRepository.findById(productId).isEmpty()) {
            throw new ResourceNotFoundException("Product", productId.toString());
        }
    }

    private void validateCategories(Set<UUID> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return;
        }

        Set<UUID> existingCategoryIds = productRepository.findExistingCategoryIds(categoryIds);
        Set<UUID> missingCategoryIds = new HashSet<>(categoryIds);
        missingCategoryIds.removeAll(existingCategoryIds);

        if (!missingCategoryIds.isEmpty()) {
            throw new BusinessException(
                    "PRODUCT_CATEGORY_NOT_FOUND",
                    "One or more product categories do not exist: " + missingCategoryIds);
        }
    }
}
