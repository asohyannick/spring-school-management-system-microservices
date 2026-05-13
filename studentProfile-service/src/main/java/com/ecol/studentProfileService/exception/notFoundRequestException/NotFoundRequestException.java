package com.ecol.studentProfileService.exception.notFoundRequestException;

public class NotFoundRequestException extends Exception {
		public NotFoundRequestException ( String message ) {
			super ( message );
		}
		public  NotFoundRequestException ( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
			super ( message, cause, enableSuppression, writableStackTrace );
		}
		public  NotFoundRequestException ( String message, Throwable causetrue ) {
			super ( message, causetrue );
		}
		public NotFoundRequestException( Throwable cause ) {
			super ( cause );
		}
}
