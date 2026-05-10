package com.maksim.auth_service.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credentials used to sign in")
public record LoginRequest(
        @Schema(description = "User email")
        @NotBlank
        String email,
        @Schema(description = "User password")
        @NotBlank
        String password
) {

}
