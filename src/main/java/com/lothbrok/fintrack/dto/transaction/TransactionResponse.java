package com.lothbrok.fintrack.dto.transaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import com.lothbrok.fintrack.entity.enums.TransactionType;

public record TransactionResponse(UUID id,
        UUID userId,
        UUID sharedWalletId,
        String title,
        BigDecimal amount,
        String currency,
        String category,
        TransactionType type,
        LocalDate eventDate,
        String note,
        String attachmentUrl,
        Instant createdAt,
        Instant updatedAt) {
}
