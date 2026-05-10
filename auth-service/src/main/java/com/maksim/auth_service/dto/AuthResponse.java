package com.maksim.auth_service.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Authentication tokens returned after successful authorization")
public record AuthResponse(
        @Schema(description = "JWT access token")
        String accessToken,
        @Schema(description = "Refresh token")
        UUID refreshToken
) {
    public static AuthResponse of(String accessToken, UUID refreshToken) {
        return new AuthResponse(accessToken, refreshToken);
    }
}
