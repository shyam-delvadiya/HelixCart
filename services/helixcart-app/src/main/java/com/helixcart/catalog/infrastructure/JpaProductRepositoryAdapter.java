package com.helixcart.catalog.infrastructure;

import com.helixcart.catalog.application.ProductCommand;
import com.helixcart.catalog.application.ProductRepository;
import com.helixcart.catalog.domain.CategorySummary;
import com.helixcart.catalog.domain.Product;
import com.helixcart.catalog.domain.ProductStatus;
import com.helixcart.common.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * JPA implementation of the product catalog persistence port.
 *
 * <p>The adapter translates between domain objects and JPA entities. This keeps
 * the application service free from table names, joins, entity graphs, and
 * Spring Data repository details.
 */
@Repository
class JpaProductRepositoryAdapter implements ProductRepository {

    private final JpaProductRepository productRepository;
    private final JpaCategoryRepository categoryRepository;

    JpaProductRepositoryAdapter(
            JpaProductRepository productRepository,
            JpaCategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product save(ProductCommand command) {
        ProductEntity entity = ProductEntity.create(
                command.sku(),
                command.name(),
                command.description(),
                command.price(),
                command.status(),
                loadCategories(command.categoryIds()));
        return toDomain(productRepository.save(entity));
    }

    @Override
    public Product update(UUID productId, ProductCommand command) {
        ProductEntity entity = productRepository.findWithCategoriesById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId.toString()));
        entity.apply(
                command.sku(),
                command.name(),
                command.description(),
                command.price(),
                command.status(),
                loadCategories(command.categoryIds()));
        return toDomain(productRepository.save(entity));
    }

    @Override
    public Product updateStatus(UUID productId, ProductStatus status) {
        ProductEntity entity = productRepository.findWithCategoriesById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId.toString()));
        entity.setStatus(status);
        return toDomain(productRepository.save(entity));
    }

    @Override
    public Optional<Product> findById(UUID productId) {
        return productRepository.findWithCategoriesById(productId).map(this::toDomain);
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        return productRepository.findWithCategoriesBySkuIgnoreCase(sku).map(this::toDomain);
    }

    @Override
    public boolean existsBySku(String sku) {
        return productRepository.existsBySkuIgnoreCase(sku);
    }

    @Override
    public boolean existsBySkuAndIdNot(String sku, UUID productId) {
        return productRepository.existsBySkuIgnoreCaseAndIdNot(sku, productId);
    }

    @Override
    public Page<Product> search(String query, ProductStatus status, Pageable pageable) {
        String normalizedQuery = query == null || query.isBlank() ? null : query.trim();
        return productRepository.search(normalizedQuery, status, pageable).map(this::toDomain);
    }

    @Override
    public Set<UUID> findExistingCategoryIds(Set<UUID> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return Set.of();
        }
        return categoryRepository.findByIdIn(categoryIds).stream()
                .map(CategoryEntity::getId)
                .collect(Collectors.toSet());
    }

    private Set<CategoryEntity> loadCategories(Set<UUID> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(categoryRepository.findByIdIn(categoryIds));
    }

    private Product toDomain(ProductEntity entity) {
        Set<CategorySummary> categories = entity.getCategories().stream()
                .map(category -> new CategorySummary(category.getId(), category.getName(), category.getSlug()))
                .collect(Collectors.toSet());

        return new Product(
                entity.getId(),
                entity.getSku(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getStatus(),
                categories,
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
