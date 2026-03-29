package com.maksim.auth_service.dto;

public record RegisterRequestDto(String email,
                                 String handle,
                                 String password) {
}
