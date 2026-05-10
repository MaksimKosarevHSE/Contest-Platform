package com.maksim.auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Register a new user")
public record RegisterRequest(
        @Schema(description = "Email")
        String email,
        @Schema(description = "Public handle")
        String handle,
        @Schema(description = "Password")
        String password
) {
}
