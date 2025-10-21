package com.lothbrok.fintrack.dto.user;

import java.time.Instant;
import java.util.UUID;

import com.lothbrok.fintrack.entity.enums.UserRole;

public record UserProfileResponse(UUID id, String fullName, String email, UserRole role, String avatarUrl,
        boolean twoFactorEnabled, Instant createdAt, Instant updatedAt) {
}
