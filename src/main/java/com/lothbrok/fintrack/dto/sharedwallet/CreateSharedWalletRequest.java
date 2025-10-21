package com.lothbrok.fintrack.dto.sharedwallet;

import java.util.List;

public record CreateSharedWalletRequest(String name, List<SharedWalletMemberUpsertRequest> members) {
}
