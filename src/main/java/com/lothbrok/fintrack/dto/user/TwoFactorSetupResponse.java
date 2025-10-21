package com.lothbrok.fintrack.dto.user;

public record TwoFactorSetupResponse(String secret, String qrCodeUrl) {
}
