package com.ecol.attendanceService.exception.conflictRequestException;

public class ConflictRequestException extends Exception {
		public ConflictRequestException ( String message ) {
			super ( message );
		}
		public  ConflictRequestException ( String message, Throwable t ) {
			super ( message, t );
		}
		public ConflictRequestException(Throwable t ) {
			super(t);
		}
		public  ConflictRequestException ( ) {
			super ( "Duplicate request exception. Please check your request and try again." );
		}
}
