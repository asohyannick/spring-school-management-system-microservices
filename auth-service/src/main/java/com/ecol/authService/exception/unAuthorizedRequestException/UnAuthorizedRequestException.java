package com.ecol.authService.exception.unAuthorizedRequestException;

public class UnAuthorizedRequestException extends Exception {
    public UnAuthorizedRequestException ( String message) {
        super(message);
    }
    public UnAuthorizedRequestException ( String message, Throwable cause) {
        super(message, cause);
    }
    public UnAuthorizedRequestException ( Throwable cause) {
        super(cause);
    }
}
