package com.helixcart.catalog.api;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Stable pagination payload returned inside {@code ApiResponse}.
 *
 * <p>Spring's {@link Page} type is convenient internally, but exposing it
 * directly would leak framework-specific fields into the public API. This
 * record keeps the external response small and predictable.
 *
 * @param <T> item type contained in the page
 */
@Schema(description = "Paginated API response data")
public record PageResponse<T>(
        List<T> items,
        int page,
        int size,
        long totalItems,
        int totalPages
) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }
}
