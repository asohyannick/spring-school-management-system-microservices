package com.ecol.attendanceService.exception.internalServerException;

public class InternalServerException extends Exception {
		public InternalServerException ( String message ) {
			super ( message );
		}
		public InternalServerException ( String message, Throwable t ) {
			super( message, t );
		}
		public InternalServerException (Throwable t ) {
			super( t );
		}
		public InternalServerException ( ) {
			super("Internal Server Error. Please try again later.");
		}
}
