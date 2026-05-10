package com.maksim.auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Validated user information extracted from token")
public record ValidateResponse(
        @Schema(description = "User's ID")
        Integer id,
        @Schema(description = "User's handle")
        String handle
) {
}
