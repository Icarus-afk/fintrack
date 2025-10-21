package com.lothbrok.fintrack.dto.mapper;

import com.lothbrok.fintrack.dto.notification.NotificationResponse;
import com.lothbrok.fintrack.entity.Notification;

public final class NotificationMapper {

    private NotificationMapper() {
    }

    public static NotificationResponse toResponse(Notification entity) {
        if (entity == null) {
            return null;
        }
        return new NotificationResponse(
                entity.getId(),
                entity.getUser() != null ? entity.getUser().getId() : null,
                entity.getType(),
                entity.getTitle(),
                entity.getMessage(),
                entity.getMetadata(),
                entity.isRead(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
