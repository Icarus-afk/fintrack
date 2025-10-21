package com.lothbrok.fintrack.dto.mapper;

import java.math.BigDecimal;

import com.lothbrok.fintrack.dto.transaction.CreateTransactionRequest;
import com.lothbrok.fintrack.dto.transaction.TransactionResponse;
import com.lothbrok.fintrack.dto.transaction.TransactionSummaryResponse;
import com.lothbrok.fintrack.dto.transaction.UpdateTransactionRequest;
import com.lothbrok.fintrack.entity.SharedWallet;
import com.lothbrok.fintrack.entity.Transaction;
import com.lothbrok.fintrack.entity.User;

public final class TransactionMapper {

    private TransactionMapper() {
    }

    public static Transaction toEntity(CreateTransactionRequest request, User user, SharedWallet sharedWallet) {
        if (request == null) {
            throw new IllegalArgumentException("request must not be null");
        }
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }
        Transaction entity = new Transaction();
        entity.setUser(user);
        entity.setSharedWallet(sharedWallet);
        entity.setTitle(request.title());
        entity.setAmount(request.amount());
        entity.setCurrency(request.currency() != null ? request.currency() : "USD");
        entity.setCategory(request.category());
        entity.setType(request.type());
        entity.setEventDate(request.eventDate());
        entity.setNote(request.note());
        entity.setAttachmentUrl(request.attachmentUrl());
        return entity;
    }

    public static void updateEntity(Transaction entity, UpdateTransactionRequest request, SharedWallet sharedWallet) {
        if (entity == null) {
            throw new IllegalArgumentException("entity must not be null");
        }
        if (request == null) {
            return;
        }
        if (request.title() != null) {
            entity.setTitle(request.title());
        }
        if (request.amount() != null) {
            entity.setAmount(request.amount());
        }
        if (request.currency() != null) {
            entity.setCurrency(request.currency());
        }
        if (request.category() != null) {
            entity.setCategory(request.category());
        }
        if (request.type() != null) {
            entity.setType(request.type());
        }
        if (request.eventDate() != null) {
            entity.setEventDate(request.eventDate());
        }
        if (request.note() != null) {
            entity.setNote(request.note());
        }
        if (request.attachmentUrl() != null) {
            entity.setAttachmentUrl(request.attachmentUrl());
        }
        entity.setSharedWallet(sharedWallet);
    }

    public static TransactionResponse toResponse(Transaction entity) {
        if (entity == null) {
            return null;
        }
        return new TransactionResponse(
                entity.getId(),
                entity.getUser() != null ? entity.getUser().getId() : null,
                entity.getSharedWallet() != null ? entity.getSharedWallet().getId() : null,
                entity.getTitle(),
                entity.getAmount(),
                entity.getCurrency(),
                entity.getCategory(),
                entity.getType(),
                entity.getEventDate(),
                entity.getNote(),
                entity.getAttachmentUrl(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public static TransactionSummaryResponse toSummary(BigDecimal totalIncome, BigDecimal totalExpense) {
        totalIncome = totalIncome != null ? totalIncome : BigDecimal.ZERO;
        totalExpense = totalExpense != null ? totalExpense : BigDecimal.ZERO;
        BigDecimal netBalance = totalIncome.subtract(totalExpense);
        return new TransactionSummaryResponse(totalIncome, totalExpense, netBalance);
    }
}
