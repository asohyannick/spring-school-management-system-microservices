package com.ecol.authService.config.customResponseMessage;

import java.time.Instant;

public record CustomResponseMessage<T>(
     String message,
     int statusCode,
     T data,
     Instant time
) {}
