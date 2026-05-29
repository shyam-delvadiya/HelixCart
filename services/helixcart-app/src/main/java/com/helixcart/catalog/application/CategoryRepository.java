package com.helixcart.catalog.application;

import com.helixcart.catalog.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Persistence port required by category use cases.
 *
 * <p>This interface keeps the application service independent from Spring Data
 * and from table-level details such as indexes or joins.
 */
public interface CategoryRepository {

    Category save(CategoryCommand command);

    Category update(UUID categoryId, CategoryCommand command);

    Optional<Category> findById(UUID categoryId);

    Optional<Category> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, UUID categoryId);

    Page<Category> search(String query, Pageable pageable);
}
