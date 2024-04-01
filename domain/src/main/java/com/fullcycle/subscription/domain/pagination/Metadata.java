package com.fullcycle.subscription.domain.pagination;

public record Metadata(
        int currentPage,
        int perPage,
        long total
) {
}
