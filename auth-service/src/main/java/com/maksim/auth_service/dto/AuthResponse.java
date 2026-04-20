package com.maksim.auth_service.dto;


import java.util.UUID;

public record AuthResponse(
        String accessToken,
        UUID refreshToken
) {
    public static AuthResponse of(String accessToken, UUID refreshToken) {
        return new AuthResponse(accessToken, refreshToken);
    }
}
