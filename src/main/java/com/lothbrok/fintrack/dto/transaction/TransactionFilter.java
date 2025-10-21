package com.lothbrok.fintrack.dto.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import com.lothbrok.fintrack.entity.enums.TransactionType;

public record TransactionFilter(LocalDate from,
        LocalDate to,
        Set<String> categories,
        TransactionType type,
        BigDecimal minAmount,
        BigDecimal maxAmount,
        UUID sharedWalletId) {
}
