package com.helixcart.inventory.api;

import com.helixcart.common.api.ApiResponse;
import com.helixcart.inventory.application.InventoryCatalog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

/**
 * REST API for inventory stock and reservation operations.
 *
 * <p>The controller exposes operational inventory commands while keeping stock
 * rules in {@link InventoryCatalog}. Responses always include available stock
 * so clients do not need to recompute it.
 */
@RestController
@RequestMapping("/api/v1/inventory")
@Tag(name = "Inventory", description = "Stock and reservation management")
class InventoryController {

    private final InventoryCatalog inventoryCatalog;

    InventoryController(InventoryCatalog inventoryCatalog) {
        this.inventoryCatalog = inventoryCatalog;
    }

    @PostMapping
    @Operation(summary = "Create inventory for a product")
    ResponseEntity<ApiResponse<InventoryResponse>> createInventory(@Valid @RequestBody InventoryRequest request) {
        InventoryResponse response = InventoryMapper.toResponse(inventoryCatalog.create(InventoryMapper.toCommand(request)));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/products/{productId}")
                .buildAndExpand(response.productId())
                .toUri();

        return ResponseEntity.created(location).body(ApiResponse.ok(response));
    }

    @GetMapping("/products/{productId}")
    @Operation(summary = "Get inventory by product id")
    ApiResponse<InventoryResponse> getByProduct(@PathVariable UUID productId) {
        return ApiResponse.ok(InventoryMapper.toResponse(inventoryCatalog.getByProduct(productId)));
    }

    @PatchMapping("/products/{productId}/stock")
    @Operation(summary = "Adjust product stock on hand")
    ApiResponse<InventoryResponse> adjustStock(
            @PathVariable UUID productId,
            @Valid @RequestBody InventoryQuantityRequest request) {
        return ApiResponse.ok(InventoryMapper.toResponse(
                inventoryCatalog.adjustStock(productId, request.quantity())));
    }

    @PatchMapping("/products/{productId}/reservations")
    @Operation(summary = "Reserve product inventory")
    ApiResponse<InventoryResponse> reserve(
            @PathVariable UUID productId,
            @Valid @RequestBody InventoryReservationRequest request) {
        return ApiResponse.ok(InventoryMapper.toResponse(
                inventoryCatalog.reserve(productId, request.quantity())));
    }

    @PatchMapping("/products/{productId}/reservations/release")
    @Operation(summary = "Release reserved product inventory")
    ApiResponse<InventoryResponse> release(
            @PathVariable UUID productId,
            @Valid @RequestBody InventoryReservationRequest request) {
        return ApiResponse.ok(InventoryMapper.toResponse(
                inventoryCatalog.release(productId, request.quantity())));
    }
}
