package com.ecol.authService.exception;
public class InternalServalErrorExceptionHandler extends RuntimeException {
    public InternalServalErrorExceptionHandler(String message) {
        super(message);
    }
    public InternalServalErrorExceptionHandler(String message, Throwable cause) {
        super(message, cause);
    }
    public InternalServalErrorExceptionHandler(Throwable cause) {
        super(cause);
    }
}
