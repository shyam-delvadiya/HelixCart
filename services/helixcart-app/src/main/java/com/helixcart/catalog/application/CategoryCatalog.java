package com.helixcart.catalog.application;

import com.helixcart.catalog.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Public application port for category catalog use cases.
 *
 * <p>The API layer calls this interface, and future modules can use it for
 * category lookups without coupling to HTTP or JPA.
 */
public interface CategoryCatalog {

    Category create(CategoryCommand command);

    Category update(UUID categoryId, CategoryCommand command);

    Category get(UUID categoryId);

    Optional<Category> findBySlug(String slug);

    Page<Category> search(String query, Pageable pageable);
}
