package com.ecol.admissionService.exception.internalServerException;

public class InternalServerException extends Exception {
	public InternalServerException ( String message ) {
		super ( message );
	}
	public InternalServerException ( String message, Throwable cause ) {
		super( message, cause );
	}
	public InternalServerException ( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
		super( message, cause, enableSuppression, writableStackTrace );
	}
	public InternalServerException(Throwable cause) {
		super(cause);
	}
	public InternalServerException() {
		super("Internal Server Error. Please try again later.");
	}
}
