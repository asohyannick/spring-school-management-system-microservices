package com.ecol.paymentService.exception.conflictRequestException;

import com.ecol.paymentService.service.PaymentService;

public class ConflictRequestException extends Exception {
		public ConflictRequestException ( String message ) {
			super ( message );
		}
		public  ConflictRequestException ( String message, Throwable cause ) {
			super ( message, cause );
		}
		public ConflictRequestException (Throwable cause ) {
			super (cause );
		}
}
