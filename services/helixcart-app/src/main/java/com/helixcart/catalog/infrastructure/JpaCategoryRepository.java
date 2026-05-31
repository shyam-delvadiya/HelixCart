package com.helixcart.catalog.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data repository for category persistence.
 *
 * <p>Single-category lookups load the parent relationship with an entity graph
 * because API responses include parent summaries. Search also loads the parent
 * summary, which is safe here because it is a many-to-one relationship.
 */
interface JpaCategoryRepository extends JpaRepository<CategoryEntity, UUID> {

    List<CategoryEntity> findByIdIn(Collection<UUID> ids);

    @EntityGraph(attributePaths = "parent")
    Optional<CategoryEntity> findWithParentById(UUID id);

    @EntityGraph(attributePaths = "parent")
    Optional<CategoryEntity> findWithParentBySlugIgnoreCase(String slug);

    boolean existsBySlugIgnoreCase(String slug);

    boolean existsBySlugIgnoreCaseAndIdNot(String slug, UUID id);

    @EntityGraph(attributePaths = "parent")
    @Query("""
            select c
            from CategoryEntity c
            where :query is null
               or lower(c.name) like lower(concat('%', :query, '%'))
               or lower(c.slug) like lower(concat('%', :query, '%'))
               or lower(coalesce(c.description, '')) like lower(concat('%', :query, '%'))
            """)
    Page<CategoryEntity> search(@Param("query") String query, Pageable pageable);
}
