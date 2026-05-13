package com.ecol.admissionService.exception.badRequestException;

public class BadRequestException extends Exception {
		public BadRequestException ( String message ) {
			super ( message );
		}
		public BadRequestException ( String message, Throwable cause ) {
			super ( message, cause );
		}
		public  BadRequestException ( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
			super ( message, cause, enableSuppression, writableStackTrace );
		}
		public BadRequestException(Throwable cause) {
			super(cause);
		}
		public BadRequestException() {
			super("Bad Request. Please check your request and try again later.");
		}
}
