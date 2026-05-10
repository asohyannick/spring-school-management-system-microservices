package com.ecol.authService.config.customResponseMessage;
public record CustomResponseMessage<T>(
     String message,
     int statusCode,
     T data
) {}
