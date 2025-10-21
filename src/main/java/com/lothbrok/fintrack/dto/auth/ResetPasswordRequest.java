package com.lothbrok.fintrack.dto.auth;

public record ResetPasswordRequest(String token, String newPassword) {
}
