package com.lothbrok.fintrack.dto.auth;

public record TokenResponse(String accessToken, String refreshToken, long expiresInSeconds) {
}
