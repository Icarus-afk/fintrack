package com.lothbrok.fintrack.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lothbrok.fintrack.entity.RecurringJob;

public interface RecurringJobRepository extends JpaRepository<RecurringJob, UUID> {

    List<RecurringJob> findByActiveTrueAndNextRunAtBefore(Instant nextRunThreshold);

    List<RecurringJob> findByUserId(UUID userId);

    Optional<RecurringJob> findByUserIdAndTemplateTransactionId(UUID userId, UUID transactionId);
}
