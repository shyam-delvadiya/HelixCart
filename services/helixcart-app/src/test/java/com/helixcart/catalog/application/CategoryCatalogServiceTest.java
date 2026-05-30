package com.helixcart.catalog.application;

import com.helixcart.catalog.domain.Category;
import com.helixcart.catalog.domain.CategorySummary;
import com.helixcart.common.exception.BusinessException;
import com.helixcart.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CategoryCatalogServiceTest {

    private final InMemoryCategoryRepository repository = new InMemoryCategoryRepository();
    private final CategoryCatalogService service = new CategoryCatalogService(repository);

    @Test
    void create_withValidRequest_returnsCreatedCategory() {
        Category category = service.create(command("Keyboards", "keyboards", null));

        assertThat(category.id()).isNotNull();
        assertThat(category.name()).isEqualTo("Keyboards");
        assertThat(category.slug()).isEqualTo("keyboards");
    }

    @Test
    void create_withDuplicateSlug_throwsBusinessException() {
        service.create(command("Keyboards", "keyboards", null));

        assertThatThrownBy(() -> service.create(command("Keyboard Gear", "KEYBOARDS", null)))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Category slug already exists");
    }

    @Test
    void create_withMissingParent_throwsBusinessException() {
        UUID parentId = UUID.randomUUID();

        assertThatThrownBy(() -> service.create(command("Mechanical", "mechanical", parentId)))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Parent category does not exist");
    }

    @Test
    void update_withSelfAsParent_throwsBusinessException() {
        Category category = service.create(command("Keyboards", "keyboards", null));

        assertThatThrownBy(() -> service.update(
                category.id(),
                command("Keyboards", "keyboards", category.id())))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Category cannot be its own parent");
    }

    @Test
    void update_withUnknownCategory_throwsResourceNotFoundException() {
        UUID categoryId = UUID.randomUUID();

        assertThatThrownBy(() -> service.update(categoryId, command("Keyboards", "keyboards", null)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Category not found with id: " + categoryId);
    }

    @Test
    void create_withExistingParent_returnsChildCategory() {
        Category parent = service.create(command("Accessories", "accessories", null));

        Category child = service.create(command("Keyboards", "keyboards", parent.id()));

        assertThat(child.parent()).isNotNull();
        assertThat(child.parent().id()).isEqualTo(parent.id());
        assertThat(child.parent().slug()).isEqualTo("accessories");
    }

    private CategoryCommand command(String name, String slug, UUID parentId) {
        return new CategoryCommand(name, slug, "Category description", parentId);
    }

    private static class InMemoryCategoryRepository implements CategoryRepository {

        private final java.util.Map<UUID, Category> categories = new java.util.HashMap<>();

        @Override
        public Category save(CategoryCommand command) {
            Category parent = command.parentId() == null ? null : categories.get(command.parentId());
            Category category = new Category(
                    UUID.randomUUID(),
                    command.name(),
                    command.slug(),
                    command.description(),
                    parent == null ? null : new CategorySummary(parent.id(), parent.name(), parent.slug()),
                    OffsetDateTime.now(),
                    OffsetDateTime.now());
            categories.put(category.id(), category);
            return category;
        }

        @Override
        public Category update(UUID categoryId, CategoryCommand command) {
            Category parent = command.parentId() == null ? null : categories.get(command.parentId());
            Category category = new Category(
                    categoryId,
                    command.name(),
                    command.slug(),
                    command.description(),
                    parent == null ? null : new CategorySummary(parent.id(), parent.name(), parent.slug()),
                    OffsetDateTime.now(),
                    OffsetDateTime.now());
            categories.put(categoryId, category);
            return category;
        }

        @Override
        public Optional<Category> findById(UUID categoryId) {
            return Optional.ofNullable(categories.get(categoryId));
        }

        @Override
        public Optional<Category> findBySlug(String slug) {
            return categories.values().stream()
                    .filter(category -> category.slug().equalsIgnoreCase(slug))
                    .findFirst();
        }

        @Override
        public boolean existsBySlug(String slug) {
            return findBySlug(slug).isPresent();
        }

        @Override
        public boolean existsBySlugAndIdNot(String slug, UUID categoryId) {
            return categories.values().stream()
                    .anyMatch(category -> category.slug().equalsIgnoreCase(slug)
                            && !category.id().equals(categoryId));
        }

        @Override
        public Page<Category> search(String query, Pageable pageable) {
            return Page.empty(pageable);
        }
    }
}
