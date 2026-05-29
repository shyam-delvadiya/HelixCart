package com.helixcart.catalog.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * HTTP request body for creating or replacing a category.
 *
 * <p>The slug pattern keeps public category URLs predictable while the domain
 * normalizes accepted slugs to lowercase.
 */
@Schema(description = "Category create or update request")
public record CategoryRequest(
        @Schema(example = "Keyboards")
        @NotBlank(message = "Name is required")
        @Size(max = 255, message = "Name must be at most 255 characters")
        String name,

        @Schema(example = "keyboards")
        @NotBlank(message = "Slug is required")
        @Size(max = 255, message = "Slug must be at most 255 characters")
        @Pattern(
                regexp = "^[a-zA-Z0-9]+(?:-[a-zA-Z0-9]+)*$",
                message = "Slug must contain letters, numbers, and single hyphens only")
        String slug,

        @Schema(example = "Input devices and accessories")
        String description,

        UUID parentId
) {
}
