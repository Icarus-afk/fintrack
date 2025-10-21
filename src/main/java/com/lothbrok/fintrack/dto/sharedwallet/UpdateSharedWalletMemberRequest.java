package com.lothbrok.fintrack.dto.sharedwallet;

import java.math.BigDecimal;

public record UpdateSharedWalletMemberRequest(Boolean admin,
        BigDecimal shareRatio) {
}
