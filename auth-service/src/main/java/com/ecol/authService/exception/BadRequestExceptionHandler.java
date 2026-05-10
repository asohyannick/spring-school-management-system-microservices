package com.ecol.authService.exception;

public class BadRequestExceptionHandler extends RuntimeException {
    public BadRequestExceptionHandler(String message) {
        super(message);
    }
    public BadRequestExceptionHandler(Throwable cause, String message) {
        super(message, cause);
    }

    public BadRequestExceptionHandler(Throwable cause) {
        super(cause);
    }
}
