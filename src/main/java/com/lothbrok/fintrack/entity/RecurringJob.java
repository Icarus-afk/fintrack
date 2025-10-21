package com.lothbrok.fintrack.entity;

import java.time.Instant;
import java.util.UUID;

import com.lothbrok.fintrack.entity.base.AuditableEntity;
import com.lothbrok.fintrack.entity.enums.RecurringFrequency;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "recurring_jobs", indexes = {
        @Index(name = "idx_recurring_next_run", columnList = "next_run_at")
})
public class RecurringJob extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "template_transaction_id", nullable = false)
    private Transaction templateTransaction;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RecurringFrequency frequency;

    @Column(name = "next_run_at", nullable = false)
    private Instant nextRunAt;

    @Column(name = "last_run_at")
    private Instant lastRunAt;

    @Column(nullable = false)
    private boolean active = true;

    public RecurringJob() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Transaction getTemplateTransaction() {
        return templateTransaction;
    }

    public void setTemplateTransaction(Transaction templateTransaction) {
        this.templateTransaction = templateTransaction;
    }

    public RecurringFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(RecurringFrequency frequency) {
        this.frequency = frequency;
    }

    public Instant getNextRunAt() {
        return nextRunAt;
    }

    public void setNextRunAt(Instant nextRunAt) {
        this.nextRunAt = nextRunAt;
    }

    public Instant getLastRunAt() {
        return lastRunAt;
    }

    public void setLastRunAt(Instant lastRunAt) {
        this.lastRunAt = lastRunAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
