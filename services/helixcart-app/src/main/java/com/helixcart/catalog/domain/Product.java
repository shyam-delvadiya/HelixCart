package com.helixcart.catalog.domain;

import com.helixcart.common.exception.BusinessException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Catalog product domain model.
 *
 * <p>This class is deliberately framework-free: no JPA, Spring, HTTP, or Redis
 * annotations belong here. It represents the business concept of a product and
 * protects core invariants that must hold regardless of which adapter creates
 * or reads the product.
 *
 * <p>Current Phase 2 invariants:
 * - SKU is required.
 * - Name is required.
 * - Price must be zero or greater.
 * - Missing status defaults to {@link ProductStatus#ACTIVE}.
 */
public class Product {

    private final UUID id;
    private final String sku;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final ProductStatus status;
    private final Set<CategorySummary> categories;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;

    public Product(
            UUID id,
            String sku,
            String name,
            String description,
            BigDecimal price,
            ProductStatus status,
            Set<CategorySummary> categories,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt) {
        if (sku == null || sku.isBlank()) {
            throw new BusinessException("INVALID_PRODUCT_SKU", "Product SKU is required");
        }
        if (name == null || name.isBlank()) {
            throw new BusinessException("INVALID_PRODUCT_NAME", "Product name is required");
        }
        if (price == null || price.signum() < 0) {
            throw new BusinessException("INVALID_PRODUCT_PRICE", "Product price must be zero or greater");
        }

        this.id = id;
        this.sku = sku.trim();
        this.name = name.trim();
        this.description = description;
        this.price = price;
        this.status = Objects.requireNonNullElse(status, ProductStatus.ACTIVE);
        this.categories = categories == null
                ? Set.of()
                : Collections.unmodifiableSet(new HashSet<>(categories));
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID id() {
        return id;
    }

    public String sku() {
        return sku;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public BigDecimal price() {
        return price;
    }

    public ProductStatus status() {
        return status;
    }

    public Set<CategorySummary> categories() {
        return categories;
    }

    public OffsetDateTime createdAt() {
        return createdAt;
    }

    public OffsetDateTime updatedAt() {
        return updatedAt;
    }
}
