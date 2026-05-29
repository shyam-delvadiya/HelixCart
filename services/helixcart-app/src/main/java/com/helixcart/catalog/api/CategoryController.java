package com.helixcart.catalog.api;

import com.helixcart.catalog.application.CategoryCatalog;
import com.helixcart.common.api.ApiResponse;
import com.helixcart.common.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

/**
 * REST API for category catalog operations.
 *
 * <p>Category hierarchy is represented through {@code parentId} in requests and
 * a parent summary in responses, avoiding recursive payloads.
 */
@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Catalog - Categories", description = "Product category management")
class CategoryController {

    private static final int MAX_PAGE_SIZE = 100;

    private final CategoryCatalog categoryCatalog;

    CategoryController(CategoryCatalog categoryCatalog) {
        this.categoryCatalog = categoryCatalog;
    }

    @PostMapping
    @Operation(summary = "Create a category")
    ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = CategoryMapper.toResponse(categoryCatalog.create(CategoryMapper.toCommand(request)));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(ApiResponse.ok(response));
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "Update a category")
    ApiResponse<CategoryResponse> updateCategory(
            @PathVariable UUID categoryId,
            @Valid @RequestBody CategoryRequest request) {
        return ApiResponse.ok(CategoryMapper.toResponse(
                categoryCatalog.update(categoryId, CategoryMapper.toCommand(request))));
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "Get a category by id")
    ApiResponse<CategoryResponse> getCategory(@PathVariable UUID categoryId) {
        return ApiResponse.ok(CategoryMapper.toResponse(categoryCatalog.get(categoryId)));
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get a category by slug")
    ApiResponse<CategoryResponse> getCategoryBySlug(@PathVariable String slug) {
        return categoryCatalog.findBySlug(slug)
                .map(CategoryMapper::toResponse)
                .map(ApiResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Category", slug));
    }

    @GetMapping
    @Operation(summary = "Search categories")
    ApiResponse<PageResponse<CategoryResponse>> searchCategories(
            @Parameter(description = "Text query matched against name, slug, or description")
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        int safePage = Math.max(page, 0);
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by("name").ascending());

        return ApiResponse.ok(PageResponse.from(categoryCatalog.search(query, pageable)
                .map(CategoryMapper::toResponse)));
    }
}
