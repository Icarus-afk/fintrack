package com.lothbrok.fintrack.dto.recurring;

import java.time.Instant;
import java.util.UUID;

import com.lothbrok.fintrack.entity.enums.RecurringFrequency;

public record RecurringJobResponse(UUID id,
        UUID userId,
        UUID templateTransactionId,
        RecurringFrequency frequency,
        Instant nextRunAt,
        Instant lastRunAt,
        boolean active,
        Instant createdAt,
        Instant updatedAt) {
}
