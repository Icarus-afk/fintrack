package com.lothbrok.fintrack.dto.budget;

import java.math.BigDecimal;

public record UpdateBudgetRequest(BigDecimal amount, BigDecimal alertThreshold) {
}
