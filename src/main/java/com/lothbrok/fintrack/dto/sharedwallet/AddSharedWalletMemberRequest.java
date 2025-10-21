package com.lothbrok.fintrack.dto.sharedwallet;

import java.math.BigDecimal;
import java.util.UUID;

public record AddSharedWalletMemberRequest(UUID memberId,
        boolean admin,
        BigDecimal shareRatio) {
}
