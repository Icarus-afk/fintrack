package com.lothbrok.fintrack.dto.auth;

public record RegisterRequest(String fullName, String email, String password) {
}
