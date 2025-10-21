package com.lothbrok.fintrack.dto.budget;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record BudgetResponse(UUID id,
        UUID userId,
        String month,
        String category,
        BigDecimal amount,
        BigDecimal usedAmount,
        BigDecimal alertThreshold,
        BigDecimal remainingAmount,
        BigDecimal percentageUsed,
        Instant createdAt,
        Instant updatedAt) {
}
