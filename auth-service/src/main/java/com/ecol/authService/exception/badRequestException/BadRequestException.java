package com.ecol.authService.exception.badRequestException;

public class BadRequestException extends Exception {
    public BadRequestException ( String message) {
        super(message);
    }
    public BadRequestException ( Throwable cause, String message) {
        super(message, cause);
    }

    public BadRequestException ( Throwable cause) {
        super(cause);
    }
}
