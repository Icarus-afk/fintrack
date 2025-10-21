package com.lothbrok.fintrack.dto.sharedwallet;

import java.math.BigDecimal;
import java.util.UUID;

public record SharedWalletMemberResponse(UUID id,
        UUID walletId,
        UUID memberId,
        String memberName,
        boolean admin,
        BigDecimal shareRatio,
        BigDecimal runningBalance) {
}
