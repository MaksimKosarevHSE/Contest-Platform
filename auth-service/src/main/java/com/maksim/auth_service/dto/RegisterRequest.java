package com.maksim.auth_service.dto;

public record RegisterRequest(String email,
                              String handle,
                              String password) {
}
