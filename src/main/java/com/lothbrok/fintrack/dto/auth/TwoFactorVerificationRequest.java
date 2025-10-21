package com.lothbrok.fintrack.dto.auth;

public record TwoFactorVerificationRequest(String email, String code) {
}
