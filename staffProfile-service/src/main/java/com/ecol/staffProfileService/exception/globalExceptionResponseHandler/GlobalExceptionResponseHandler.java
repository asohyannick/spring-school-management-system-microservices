package com.ecol.staffProfileService.exception.globalExceptionResponseHandler;

import java.time.Instant;
public record GlobalExceptionResponseHandler(
		Instant timestamp,
		String message,
		String details,
		String path,
		int status,
		String statusCode,
		String errorCode,
		String method
) { }