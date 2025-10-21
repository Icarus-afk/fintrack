package com.lothbrok.fintrack.dto.mapper;

import com.lothbrok.fintrack.dto.user.UserProfileResponse;
import com.lothbrok.fintrack.entity.User;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserProfileResponse toProfileResponse(User user) {
        if (user == null) {
            return null;
        }
        return new UserProfileResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.getAvatarUrl(),
                user.getTwoFactorSecret() != null,
                user.getCreatedAt(),
                user.getUpdatedAt());
    }
}
