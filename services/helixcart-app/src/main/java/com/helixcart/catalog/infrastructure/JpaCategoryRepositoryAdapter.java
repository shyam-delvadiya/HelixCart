package com.helixcart.catalog.infrastructure;

import com.helixcart.catalog.application.CategoryCommand;
import com.helixcart.catalog.application.CategoryRepository;
import com.helixcart.catalog.domain.Category;
import com.helixcart.catalog.domain.CategorySummary;
import com.helixcart.common.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA implementation of the category persistence port.
 *
 * <p>Slug normalization is performed before database lookups and writes so the
 * application can treat slugs as case-insensitive identifiers while preserving a
 * simple unique constraint in PostgreSQL.
 */
@Repository
class JpaCategoryRepositoryAdapter implements CategoryRepository {

    private final JpaCategoryRepository categoryRepository;

    JpaCategoryRepositoryAdapter(JpaCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category save(CategoryCommand command) {
        CategoryEntity entity = CategoryEntity.create(
                command.name(),
                Category.normalizeSlug(command.slug()),
                command.description(),
                loadParent(command.parentId()));
        return toDomain(categoryRepository.save(entity));
    }

    @Override
    public Category update(UUID categoryId, CategoryCommand command) {
        CategoryEntity entity = categoryRepository.findWithParentById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", categoryId.toString()));
        entity.apply(
                command.name(),
                Category.normalizeSlug(command.slug()),
                command.description(),
                loadParent(command.parentId()));
        return toDomain(categoryRepository.save(entity));
    }

    @Override
    public Optional<Category> findById(UUID categoryId) {
        return categoryRepository.findWithParentById(categoryId).map(this::toDomain);
    }

    @Override
    public Optional<Category> findBySlug(String slug) {
        return categoryRepository.findWithParentBySlugIgnoreCase(Category.normalizeSlug(slug)).map(this::toDomain);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return categoryRepository.existsBySlugIgnoreCase(Category.normalizeSlug(slug));
    }

    @Override
    public boolean existsBySlugAndIdNot(String slug, UUID categoryId) {
        return categoryRepository.existsBySlugIgnoreCaseAndIdNot(Category.normalizeSlug(slug), categoryId);
    }

    @Override
    public Page<Category> search(String query, Pageable pageable) {
        String normalizedQuery = query == null || query.isBlank() ? null : query.trim();
        return categoryRepository.search(normalizedQuery, pageable).map(this::toDomain);
    }

    private CategoryEntity loadParent(UUID parentId) {
        if (parentId == null) {
            return null;
        }
        return categoryRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", parentId.toString()));
    }

    private Category toDomain(CategoryEntity entity) {
        CategorySummary parent = entity.getParent() == null
                ? null
                : new CategorySummary(
                        entity.getParent().getId(),
                        entity.getParent().getName(),
                        entity.getParent().getSlug());

        return new Category(
                entity.getId(),
                entity.getName(),
                entity.getSlug(),
                entity.getDescription(),
                parent,
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
