package com.lothbrok.fintrack.dto.transaction;

import java.math.BigDecimal;

public record TransactionSummaryResponse(BigDecimal totalIncome,
        BigDecimal totalExpense,
        BigDecimal netBalance) {
}
