package com.lothbrok.fintrack.dto.notification;

import java.time.Instant;
import java.util.UUID;

import com.lothbrok.fintrack.entity.enums.NotificationType;

public record NotificationResponse(UUID id,
        UUID userId,
        NotificationType type,
        String title,
        String message,
        String metadata,
        boolean read,
        Instant createdAt,
        Instant updatedAt) {
}
