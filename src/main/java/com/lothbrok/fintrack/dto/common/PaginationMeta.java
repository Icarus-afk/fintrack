package com.lothbrok.fintrack.dto.common;

import java.io.Serializable;

public record PaginationMeta(int page, int size, long totalElements, int totalPages) implements Serializable {

    public PaginationMeta {
        if (page < 0) {
            throw new IllegalArgumentException("page must be >= 0");
        }
        if (size < 1) {
            throw new IllegalArgumentException("size must be >= 1");
        }
        if (totalElements < 0) {
            throw new IllegalArgumentException("totalElements must be >= 0");
        }
        if (totalPages < 0) {
            throw new IllegalArgumentException("totalPages must be >= 0");
        }
    }
}
