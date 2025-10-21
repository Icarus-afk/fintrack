package com.lothbrok.fintrack.dto.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.lothbrok.fintrack.entity.enums.TransactionType;

public record UpdateTransactionRequest(String title,
        BigDecimal amount,
        String currency,
        String category,
        TransactionType type,
        LocalDate eventDate,
        String note,
        UUID sharedWalletId,
        String attachmentUrl) {
}
