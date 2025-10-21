package com.lothbrok.fintrack.dto.budget;

import java.math.BigDecimal;

public record CreateBudgetRequest(String month,
        String category,
        BigDecimal amount,
        BigDecimal alertThreshold) {
}
