package com.lothbrok.fintrack.dto.user;

public record AvatarUploadResponse(String fileName, String url, long size, String contentType) {
}
