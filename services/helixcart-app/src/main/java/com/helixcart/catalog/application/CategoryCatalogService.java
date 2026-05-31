package com.helixcart.catalog.application;

import com.helixcart.catalog.domain.Category;
import com.helixcart.common.exception.BusinessException;
import com.helixcart.common.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Category catalog application service.
 *
 * <p>This service owns hierarchy and uniqueness rules for categories:
 * - Slugs must be unique.
 * - Parent categories must exist.
 * - A category cannot be its own parent.
 *
 * <p>Keeping these checks here makes the API thin and leaves the infrastructure
 * adapter focused on database mapping.
 */
@Service
public class CategoryCatalogService implements CategoryCatalog {

    private final CategoryRepository categoryRepository;

    public CategoryCatalogService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public Category create(CategoryCommand command) {
        validateParent(command.parentId(), null);
        if (categoryRepository.existsBySlug(command.slug())) {
            throw new BusinessException("CATEGORY_SLUG_ALREADY_EXISTS", "Category slug already exists");
        }
        return categoryRepository.save(command);
    }

    @Override
    @Transactional
    public Category update(UUID categoryId, CategoryCommand command) {
        ensureCategoryExists(categoryId);
        validateParent(command.parentId(), categoryId);
        if (categoryRepository.existsBySlugAndIdNot(command.slug(), categoryId)) {
            throw new BusinessException("CATEGORY_SLUG_ALREADY_EXISTS", "Category slug already exists");
        }
        return categoryRepository.update(categoryId, command);
    }

    @Override
    @Transactional(readOnly = true)
    public Category get(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", categoryId.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> findBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Category> search(String query, Pageable pageable) {
        return categoryRepository.search(query, pageable);
    }

    private void ensureCategoryExists(UUID categoryId) {
        if (categoryRepository.findById(categoryId).isEmpty()) {
            throw new ResourceNotFoundException("Category", categoryId.toString());
        }
    }

    private void validateParent(UUID parentId, UUID categoryId) {
        if (parentId == null) {
            return;
        }
        if (parentId.equals(categoryId)) {
            throw new BusinessException("CATEGORY_PARENT_SELF_REFERENCE", "Category cannot be its own parent");
        }
        if (categoryRepository.findById(parentId).isEmpty()) {
            throw new BusinessException("CATEGORY_PARENT_NOT_FOUND", "Parent category does not exist");
        }
    }
}
