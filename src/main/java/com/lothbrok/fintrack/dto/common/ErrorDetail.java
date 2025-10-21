package com.lothbrok.fintrack.dto.common;

import java.io.Serializable;
import java.util.Map;

public record ErrorDetail(String code, String message, Map<String, Object> details) implements Serializable {

    public ErrorDetail {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("code must not be blank");
        }
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("message must not be blank");
        }
        details = details == null || details.isEmpty() ? null : Map.copyOf(details);
    }
}
