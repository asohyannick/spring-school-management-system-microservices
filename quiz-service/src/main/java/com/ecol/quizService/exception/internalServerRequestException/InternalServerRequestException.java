package com.ecol.quizService.exception.internalServerRequestException;

public class InternalServerRequestException extends Exception {
		public InternalServerRequestException ( String message ) {
			super ( message );
		}
		
		public InternalServerRequestException ( String message, Throwable cause ) {
			super ( message, cause );
		}
		
		public InternalServerRequestException ( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
			super ( message, cause, enableSuppression, writableStackTrace );
		}
		public InternalServerRequestException(Throwable cause) {
			super(cause);
		}
}
