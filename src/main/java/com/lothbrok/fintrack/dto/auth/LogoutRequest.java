package com.lothbrok.fintrack.dto.auth;

import java.util.UUID;

public record LogoutRequest(UUID refreshToken) {
}
