package com.ecol.reviewService.exception.notFoundRequestException;

public class NotFoundRequestException extends Exception {
		public NotFoundRequestException ( String message ) {
			super ( message );
		}
		public NotFoundRequestException ( String message, Throwable cause ) {
			super ( message, cause );
		}
		public NotFoundRequestException ( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
			super( message, cause, enableSuppression, writableStackTrace );
		}
		public NotFoundRequestException ( Throwable cause ) {
			super(cause);
		}
		public NotFoundRequestException() {
			super("Resource Not Found");
		}
}
