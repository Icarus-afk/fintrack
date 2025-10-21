package com.lothbrok.fintrack.dto.recurring;

import java.time.Instant;

import com.lothbrok.fintrack.entity.enums.RecurringFrequency;

public record UpdateRecurringJobRequest(RecurringFrequency frequency,
        Instant nextRunAt,
        Boolean active) {
}
