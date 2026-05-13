package com.ecol.humanResource.exception.internalServerRequestException;

public class InternalServerRequestException extends Exception {
		public InternalServerRequestException ( String message ) {
			super ( message );
		}
		public InternalServerRequestException ( String message, Throwable cause ) {
			super ( message, cause );
		}
		public InternalServerRequestException ( Throwable cause ) {
			super ( cause );
		}
}
