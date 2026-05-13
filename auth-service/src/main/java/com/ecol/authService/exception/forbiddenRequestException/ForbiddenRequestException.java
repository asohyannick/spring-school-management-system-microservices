package com.ecol.authService.exception.forbiddenRequestException;

public class ForbiddenRequestException extends Exception {
    public ForbiddenRequestException ( String message) {
        super(message);
    }
    public ForbiddenRequestException ( String message, Throwable cause) {
        super(message, cause);
    }
    public ForbiddenRequestException ( Throwable cause) {
        super(cause);
    }
}
