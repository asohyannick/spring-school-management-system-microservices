package com.ecol.admissionService.exception.conflictRequestException;

public class ConflictRequestException extends Exception {
	public ConflictRequestException ( String message ) {
		super ( message );
	}
	public  ConflictRequestException ( String message, Throwable cause ) {
		super ( message, cause );
	}
	public ConflictRequestException ( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
		super(message, cause, enableSuppression, writableStackTrace );
	}
	public ConflictRequestException(Throwable cause) {
		super(cause);
	}
	public ConflictRequestException() {
		super("Duplicate resource exception. Please check your request and try again later.");
	}
}
