package com.ecol.courseService.exception.conflictRequestException;

public class ConflictRequestException extends Exception {
		public ConflictRequestException ( String message ) {
			super ( message );
		}
		public ConflictRequestException(String message, Throwable cause) {
			super( message, cause );
		}
		public ConflictRequestException(Throwable cause) {
			super( cause );
		}
}
