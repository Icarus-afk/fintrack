package com.lothbrok.fintrack.dto.mapper;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.lothbrok.fintrack.dto.budget.BudgetResponse;
import com.lothbrok.fintrack.dto.budget.CreateBudgetRequest;
import com.lothbrok.fintrack.dto.budget.UpdateBudgetRequest;
import com.lothbrok.fintrack.entity.Budget;
import com.lothbrok.fintrack.entity.User;

public final class BudgetMapper {

    private BudgetMapper() {
    }

    public static Budget toEntity(CreateBudgetRequest request, User user) {
        if (request == null) {
            throw new IllegalArgumentException("request must not be null");
        }
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }
        Budget entity = new Budget();
        entity.setUser(user);
        entity.setMonth(request.month());
        entity.setCategory(request.category());
        entity.setAmount(request.amount());
        entity.setAlertThreshold(request.alertThreshold() != null ? request.alertThreshold() : new BigDecimal("0.80"));
        return entity;
    }

    public static void updateEntity(Budget entity, UpdateBudgetRequest request) {
        if (entity == null || request == null) {
            return;
        }
        if (request.amount() != null) {
            entity.setAmount(request.amount());
        }
        if (request.alertThreshold() != null) {
            entity.setAlertThreshold(request.alertThreshold());
        }
    }

    public static BudgetResponse toResponse(Budget entity) {
        if (entity == null) {
            return null;
        }
        BigDecimal amount = defaultZero(entity.getAmount());
        BigDecimal used = defaultZero(entity.getUsedAmount());
        BigDecimal remaining = amount.subtract(used);
        if (remaining.compareTo(BigDecimal.ZERO) < 0) {
            remaining = BigDecimal.ZERO;
        }
        BigDecimal percentageUsed = amount.signum() == 0 ? BigDecimal.ZERO
                : used.divide(amount, 4, RoundingMode.HALF_UP);
        return new BudgetResponse(
                entity.getId(),
                entity.getUser() != null ? entity.getUser().getId() : null,
                entity.getMonth(),
                entity.getCategory(),
                amount,
                used,
                entity.getAlertThreshold(),
                remaining,
                percentageUsed,
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    private static BigDecimal defaultZero(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}
