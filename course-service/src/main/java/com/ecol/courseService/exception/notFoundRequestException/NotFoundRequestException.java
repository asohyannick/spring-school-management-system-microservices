package com.ecol.courseService.exception.notFoundRequestException;

public class NotFoundRequestException extends Exception {
	public NotFoundRequestException ( String message ) {
		super ( message );
	}
	public NotFoundRequestException(String message, Throwable cause) {
		super ( message, cause );
	}
	public NotFoundRequestException(Throwable cause) {
		super ( cause );
	}
}
