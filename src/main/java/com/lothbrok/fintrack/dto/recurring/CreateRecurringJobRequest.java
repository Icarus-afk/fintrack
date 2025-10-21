package com.lothbrok.fintrack.dto.recurring;

import java.time.Instant;
import java.util.UUID;

import com.lothbrok.fintrack.entity.enums.RecurringFrequency;

public record CreateRecurringJobRequest(UUID templateTransactionId,
        RecurringFrequency frequency,
        Instant firstExecutionAt) {
}
