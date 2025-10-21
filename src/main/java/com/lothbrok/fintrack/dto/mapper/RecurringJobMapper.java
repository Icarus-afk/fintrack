package com.lothbrok.fintrack.dto.mapper;

import java.time.Instant;

import com.lothbrok.fintrack.dto.recurring.CreateRecurringJobRequest;
import com.lothbrok.fintrack.dto.recurring.RecurringJobResponse;
import com.lothbrok.fintrack.dto.recurring.UpdateRecurringJobRequest;
import com.lothbrok.fintrack.entity.RecurringJob;
import com.lothbrok.fintrack.entity.Transaction;
import com.lothbrok.fintrack.entity.User;

public final class RecurringJobMapper {

    private RecurringJobMapper() {
    }

    public static RecurringJob toEntity(CreateRecurringJobRequest request, User user, Transaction template) {
        if (request == null) {
            throw new IllegalArgumentException("request must not be null");
        }
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }
        if (template == null) {
            throw new IllegalArgumentException("template must not be null");
        }
        RecurringJob job = new RecurringJob();
        job.setUser(user);
        job.setTemplateTransaction(template);
        job.setFrequency(request.frequency());
        job.setNextRunAt(request.firstExecutionAt() != null ? request.firstExecutionAt() : Instant.now());
        job.setActive(true);
        return job;
    }

    public static void updateEntity(RecurringJob job, UpdateRecurringJobRequest request) {
        if (job == null || request == null) {
            return;
        }
        if (request.frequency() != null) {
            job.setFrequency(request.frequency());
        }
        if (request.nextRunAt() != null) {
            job.setNextRunAt(request.nextRunAt());
        }
        if (request.active() != null) {
            job.setActive(request.active());
        }
    }

    public static RecurringJobResponse toResponse(RecurringJob job) {
        if (job == null) {
            return null;
        }
        return new RecurringJobResponse(
                job.getId(),
                job.getUser() != null ? job.getUser().getId() : null,
                job.getTemplateTransaction() != null ? job.getTemplateTransaction().getId() : null,
                job.getFrequency(),
                job.getNextRunAt(),
                job.getLastRunAt(),
                job.isActive(),
                job.getCreatedAt(),
                job.getUpdatedAt());
    }
}
