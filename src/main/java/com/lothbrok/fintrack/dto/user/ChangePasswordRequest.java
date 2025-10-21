package com.lothbrok.fintrack.dto.user;

public record ChangePasswordRequest(String currentPassword, String newPassword) {
}
