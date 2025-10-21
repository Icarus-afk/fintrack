package com.lothbrok.fintrack.dto.sharedwallet;

import java.math.BigDecimal;
import java.util.UUID;

public record SharedWalletMemberUpsertRequest(UUID memberId,
        boolean admin,
        BigDecimal shareRatio) {
}
