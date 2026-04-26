package com.maksim.problemService.exception;

public class ForbiddenException extends RuntimeException{
    public ForbiddenException(String msg) {
        super(msg);
    }
}
