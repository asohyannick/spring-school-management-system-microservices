package com.ecol.userService.exception;

public class ConflictExceptionHandler extends RuntimeException {
    public ConflictExceptionHandler(String message) {
        super(message);
    }
    public ConflictExceptionHandler(String message, Throwable cause) {
        super(message, cause);
    }
    public ConflictExceptionHandler(Throwable cause) {
        super(cause);
    }
}
