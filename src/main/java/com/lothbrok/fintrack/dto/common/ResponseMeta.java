package com.lothbrok.fintrack.dto.common;

import java.io.Serializable;
import java.time.Instant;
import java.util.Optional;

public record ResponseMeta(Instant timestamp, String requestId, PaginationMeta pagination) implements Serializable {

    public ResponseMeta {
        timestamp = Optional.ofNullable(timestamp).orElse(Instant.now());
        requestId = Optional.ofNullable(requestId).orElse("n/a");
    }

    public static ResponseMeta withoutPagination(String requestId) {
        return new ResponseMeta(Instant.now(), requestId, null);
    }

    public ResponseMeta withPagination(PaginationMeta paginationMeta) {
        return new ResponseMeta(timestamp, requestId, paginationMeta);
    }
}
