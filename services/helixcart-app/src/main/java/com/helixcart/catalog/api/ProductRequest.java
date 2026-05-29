package com.helixcart.catalog.api;

import com.helixcart.catalog.domain.ProductStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

/**
 * HTTP request body for creating or replacing a product.
 *
 * <p>Validation here protects the API boundary. Deeper business invariants are
 * still enforced in the domain/application layers.
 */
@Schema(description = "Product create or update request")
public record ProductRequest(
        @Schema(example = "SKU-12345")
        @NotBlank(message = "SKU is required")
        @Size(max = 100, message = "SKU must be at most 100 characters")
        String sku,

        @Schema(example = "Wireless Keyboard")
        @NotBlank(message = "Name is required")
        @Size(max = 255, message = "Name must be at most 255 characters")
        String name,

        @Schema(example = "Low-profile mechanical keyboard")
        String description,

        @Schema(example = "89.9900")
        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0000", message = "Price must be zero or greater")
        BigDecimal price,

        @Schema(example = "ACTIVE")
        ProductStatus status,

        Set<UUID> categoryIds
) {
}
