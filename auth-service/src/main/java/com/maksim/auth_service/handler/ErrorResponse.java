package com.maksim.auth_service.handler;

public record ErrorResponse(
        String message) {
    public static ErrorResponse of(String message){
        return new ErrorResponse(message);
    }
}