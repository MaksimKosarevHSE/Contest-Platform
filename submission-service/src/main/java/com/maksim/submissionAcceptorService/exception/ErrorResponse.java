package com.maksim.submissionAcceptorService.exception;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response with error")
public record ErrorResponse(
        @Schema(description = "Error info", example = "User is not authenticated")
        String message) {}