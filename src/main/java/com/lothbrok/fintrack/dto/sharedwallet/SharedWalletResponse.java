package com.lothbrok.fintrack.dto.sharedwallet;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record SharedWalletResponse(UUID id,
        String name,
        UUID ownerId,
        List<SharedWalletMemberResponse> members,
        Instant createdAt,
        Instant updatedAt) {
}
