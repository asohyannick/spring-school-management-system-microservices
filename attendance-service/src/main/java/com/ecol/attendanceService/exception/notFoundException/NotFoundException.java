package com.ecol.attendanceService.exception.notFoundException;

public class NotFoundException extends Exception {
	public NotFoundException ( String message ) {
		super ( message );
	}
	public NotFoundException ( String message, Throwable t ) {
		super( message, t );
	}
	public NotFoundException(Throwable t ) {
		super( t );
	}
	public NotFoundException() {
		super("Resource Not Found Exception");
	}
}
