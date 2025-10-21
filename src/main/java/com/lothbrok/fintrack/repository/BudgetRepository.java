package com.lothbrok.fintrack.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lothbrok.fintrack.entity.Budget;

public interface BudgetRepository extends JpaRepository<Budget, UUID> {

    List<Budget> findByUserId(UUID userId);

    List<Budget> findByUserIdAndMonth(UUID userId, String month);

    Optional<Budget> findByUserIdAndMonthAndCategory(UUID userId, String month, String category);

    boolean existsByUserIdAndMonthAndCategory(UUID userId, String month, String category);
}
