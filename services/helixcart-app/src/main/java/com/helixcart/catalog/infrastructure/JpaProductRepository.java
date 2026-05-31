package com.helixcart.catalog.infrastructure;

import com.helixcart.catalog.domain.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data repository for product persistence.
 *
 * <p>This interface stays package-private so only the adapter can use Spring
 * Data directly. Application services depend on {@code ProductRepository}
 * instead.
 */
interface JpaProductRepository extends JpaRepository<ProductEntity, UUID> {

    @EntityGraph(attributePaths = "categories")
    Optional<ProductEntity> findWithCategoriesById(UUID id);

    @EntityGraph(attributePaths = "categories")
    Optional<ProductEntity> findWithCategoriesBySkuIgnoreCase(String sku);

    boolean existsBySkuIgnoreCase(String sku);

    boolean existsBySkuIgnoreCaseAndIdNot(String sku, UUID id);

    @Query("""
            select p
            from ProductEntity p
            where (:status is null or p.status = :status)
              and (
                  :query is null
                  or lower(p.sku) like lower(concat('%', :query, '%'))
                  or lower(p.name) like lower(concat('%', :query, '%'))
                  or lower(coalesce(p.description, '')) like lower(concat('%', :query, '%'))
              )
            """)
    Page<ProductEntity> search(
            @Param("query") String query,
            @Param("status") ProductStatus status,
            Pageable pageable);
}
