package com.lothbrok.fintrack.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lothbrok.fintrack.entity.SharedWallet;

public interface SharedWalletRepository extends JpaRepository<SharedWallet, UUID> {

    List<SharedWallet> findByOwnerId(UUID ownerId);

    boolean existsByOwnerIdAndNameIgnoreCase(UUID ownerId, String name);
}
