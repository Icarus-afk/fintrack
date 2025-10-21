package com.lothbrok.fintrack.dto.common;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Supplier;

public record ApiResponse<T>(boolean success, T data, ErrorDetail error, ResponseMeta meta) implements Serializable {

    public ApiResponse {
        if (success && error != null) {
            throw new IllegalStateException("Successful responses cannot include error details");
        }
        if (!success && error == null) {
            throw new IllegalStateException("Failed responses must include error details");
        }
        meta = Objects.requireNonNullElseGet(meta, () -> ResponseMeta.withoutPagination("n/a"));
    }

    public static <T> ApiResponse<T> success(T data, ResponseMeta meta) {
        return new ApiResponse<>(true, data, null, meta);
    }

    public static <T> ApiResponse<T> success(T data, Supplier<ResponseMeta> metaSupplier) {
        return success(data, metaSupplier == null ? null : metaSupplier.get());
    }

    public static <T> ApiResponse<T> failure(ErrorDetail error, ResponseMeta meta) {
        return new ApiResponse<>(false, null, error, meta);
    }
}
