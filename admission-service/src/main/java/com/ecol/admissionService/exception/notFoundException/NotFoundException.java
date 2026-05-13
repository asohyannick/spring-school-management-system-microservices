package com.ecol.admissionService.exception.notFoundException;

public class NotFoundException extends Exception {
	public NotFoundException ( String message, Throwable cause ) {
		super ( message, cause );
	}
	public NotFoundException ( String message ) {
		super( message );
	}
	public NotFoundException(Throwable cause ) {
		super(cause);
	}
	public NotFoundException ( ) {
		super("User not found");
	}
}
