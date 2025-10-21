package com.lothbrok.fintrack.dto.auth;

import java.util.UUID;

public record RefreshTokenRequest(UUID refreshToken) {
}
