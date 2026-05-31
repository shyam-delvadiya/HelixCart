package com.helixcart.catalog.infrastructure;

import com.helixcart.catalog.domain.ProductStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * JPA mapping for the {@code products} table.
 *
 * <p>This class is package-private on purpose. Persistence entities are an
 * infrastructure detail and must not leak into the catalog domain,
 * application, or API layers.
 */
@Entity
@Table(name = "products")
class ProductEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<CategoryEntity> categories = new HashSet<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    static ProductEntity create(
            String sku,
            String name,
            String description,
            BigDecimal price,
            ProductStatus status,
            Set<CategoryEntity> categories) {
        ProductEntity product = new ProductEntity();
        product.id = UUID.randomUUID();
        product.apply(sku, name, description, price, status, categories);
        return product;
    }

    @PrePersist
    void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }
    }

    void apply(
            String sku,
            String name,
            String description,
            BigDecimal price,
            ProductStatus status,
            Set<CategoryEntity> categories) {
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status == null ? ProductStatus.ACTIVE : status;
        this.categories.clear();
        this.categories.addAll(categories);
    }

    void setStatus(ProductStatus status) {
        this.status = status;
    }

    UUID getId() {
        return id;
    }

    String getSku() {
        return sku;
    }

    String getName() {
        return name;
    }

    String getDescription() {
        return description;
    }

    BigDecimal getPrice() {
        return price;
    }

    ProductStatus getStatus() {
        return status;
    }

    Set<CategoryEntity> getCategories() {
        return categories;
    }

    OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
