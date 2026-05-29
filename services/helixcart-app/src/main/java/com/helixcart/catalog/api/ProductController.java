package com.helixcart.catalog.api;

import com.helixcart.catalog.application.ProductCatalog;
import com.helixcart.catalog.domain.ProductStatus;
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
import org.springframework.web.bind.annotation.PatchMapping;
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
 * REST API for product catalog operations.
 *
 * <p>The controller is intentionally thin: it validates request DTOs, maps them
 * to application commands, delegates to {@link ProductCatalog}, and wraps
 * results in the common API envelope.
 */
@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Catalog - Products", description = "Product catalog management")
class ProductController {

    private static final int MAX_PAGE_SIZE = 100;

    private final ProductCatalog productCatalog;

    ProductController(ProductCatalog productCatalog) {
        this.productCatalog = productCatalog;
    }

    @PostMapping
    @Operation(summary = "Create a product")
    ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse response = ProductMapper.toResponse(productCatalog.create(ProductMapper.toCommand(request)));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(ApiResponse.ok(response));
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Update a product")
    ApiResponse<ProductResponse> updateProduct(
            @PathVariable UUID productId,
            @Valid @RequestBody ProductRequest request) {
        return ApiResponse.ok(ProductMapper.toResponse(
                productCatalog.update(productId, ProductMapper.toCommand(request))));
    }

    @PatchMapping("/{productId}/status")
    @Operation(summary = "Update a product status")
    ApiResponse<ProductResponse> updateProductStatus(
            @PathVariable UUID productId,
            @Valid @RequestBody ProductStatusRequest request) {
        return ApiResponse.ok(ProductMapper.toResponse(productCatalog.updateStatus(productId, request.status())));
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get a product by id")
    ApiResponse<ProductResponse> getProduct(@PathVariable UUID productId) {
        return ApiResponse.ok(ProductMapper.toResponse(productCatalog.get(productId)));
    }

    @GetMapping("/sku/{sku}")
    @Operation(summary = "Get a product by SKU")
    ApiResponse<ProductResponse> getProductBySku(@PathVariable String sku) {
        return productCatalog.findBySku(sku)
                .map(ProductMapper::toResponse)
                .map(ApiResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Product", sku));
    }

    @GetMapping
    @Operation(summary = "Search products")
    ApiResponse<PageResponse<ProductResponse>> searchProducts(
            @Parameter(description = "Text query matched against SKU, name, or description")
            @RequestParam(required = false) String query,
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        int safePage = Math.max(page, 0);
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by("createdAt").descending());

        return ApiResponse.ok(PageResponse.from(productCatalog.search(query, status, pageable)
                .map(ProductMapper::toResponse)));
    }
}
