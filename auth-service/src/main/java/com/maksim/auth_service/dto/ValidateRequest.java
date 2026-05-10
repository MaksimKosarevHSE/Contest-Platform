package com.maksim.auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request for token validation")
public record ValidateRequest(
        @Schema(description = "JWT token to validate")
        String token
) {
}
