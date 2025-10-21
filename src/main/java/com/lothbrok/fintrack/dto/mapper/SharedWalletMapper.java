package com.lothbrok.fintrack.dto.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.lothbrok.fintrack.dto.sharedwallet.AddSharedWalletMemberRequest;
import com.lothbrok.fintrack.dto.sharedwallet.SharedWalletMemberResponse;
import com.lothbrok.fintrack.dto.sharedwallet.SharedWalletMemberUpsertRequest;
import com.lothbrok.fintrack.dto.sharedwallet.SharedWalletResponse;
import com.lothbrok.fintrack.dto.sharedwallet.UpdateSharedWalletMemberRequest;
import com.lothbrok.fintrack.entity.SharedWallet;
import com.lothbrok.fintrack.entity.SharedWalletMember;
import com.lothbrok.fintrack.entity.User;

public final class SharedWalletMapper {

    private SharedWalletMapper() {
    }

    public static SharedWalletMember toMemberEntity(AddSharedWalletMemberRequest request, SharedWallet wallet, User user) {
        if (request == null) {
            throw new IllegalArgumentException("request must not be null");
        }
        if (wallet == null) {
            throw new IllegalArgumentException("wallet must not be null");
        }
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }
        SharedWalletMember member = new SharedWalletMember();
        member.setWallet(wallet);
        member.setMember(user);
        member.setAdmin(request.admin());
        member.setShareRatio(defaultRatio(request.shareRatio()));
        return member;
    }

    public static void updateMemberEntity(SharedWalletMember entity, UpdateSharedWalletMemberRequest request) {
        if (entity == null || request == null) {
            return;
        }
        if (request.admin() != null) {
            entity.setAdmin(request.admin());
        }
        if (request.shareRatio() != null) {
            entity.setShareRatio(defaultRatio(request.shareRatio()));
        }
    }

    public static SharedWalletMember toMemberEntity(SharedWalletMemberUpsertRequest request, SharedWallet wallet, User user) {
        if (request == null) {
            throw new IllegalArgumentException("request must not be null");
        }
        if (wallet == null) {
            throw new IllegalArgumentException("wallet must not be null");
        }
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }
        SharedWalletMember member = new SharedWalletMember();
        member.setWallet(wallet);
        member.setMember(user);
        member.setAdmin(request.admin());
        member.setShareRatio(defaultRatio(request.shareRatio()));
        return member;
    }

    public static SharedWalletResponse toResponse(SharedWallet wallet) {
        if (wallet == null) {
            return null;
        }
        List<SharedWalletMemberResponse> members = wallet.getMembers().stream()
                .map(SharedWalletMapper::toMemberResponse)
                .toList();
        return new SharedWalletResponse(
                wallet.getId(),
                wallet.getName(),
                wallet.getOwner() != null ? wallet.getOwner().getId() : null,
                members,
                wallet.getCreatedAt(),
                wallet.getUpdatedAt());
    }

    public static SharedWalletMemberResponse toMemberResponse(SharedWalletMember member) {
        if (member == null) {
            return null;
        }
        UUID memberId = member.getMember() != null ? member.getMember().getId() : null;
        String memberName = member.getMember() != null ? member.getMember().getFullName() : null;
        return new SharedWalletMemberResponse(
                member.getId(),
                member.getWallet() != null ? member.getWallet().getId() : null,
                memberId,
                memberName,
                member.isAdmin(),
                defaultRatio(member.getShareRatio()),
                defaultRatio(member.getRunningBalance()));
    }

    private static BigDecimal defaultRatio(BigDecimal ratio) {
        return Objects.requireNonNullElse(ratio, BigDecimal.ZERO);
    }
}
