package com.lothbrok.fintrack.dto.notification;

import java.util.List;
import java.util.UUID;

public record MarkNotificationsReadRequest(List<UUID> notificationIds) {
}
