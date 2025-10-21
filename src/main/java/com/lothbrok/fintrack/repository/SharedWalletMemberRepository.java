package com.lothbrok.fintrack.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lothbrok.fintrack.entity.SharedWalletMember;

public interface SharedWalletMemberRepository extends JpaRepository<SharedWalletMember, UUID> {

    List<SharedWalletMember> findByWalletId(UUID walletId);

    List<SharedWalletMember> findByMemberId(UUID memberId);

    Optional<SharedWalletMember> findByWalletIdAndMemberId(UUID walletId, UUID memberId);
}
