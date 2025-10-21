package com.lothbrok.fintrack.repository;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lothbrok.fintrack.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    Page<Transaction> findByUserId(UUID userId, Pageable pageable);

    Page<Transaction> findByUserIdAndEventDateBetween(UUID userId, LocalDate from, LocalDate to, Pageable pageable);

    Page<Transaction> findBySharedWalletId(UUID sharedWalletId, Pageable pageable);

    long countByUserIdAndEventDateBetween(UUID userId, LocalDate from, LocalDate to);
}
