package com.ecol.userService.config.customResponseMessage;
public record CustomResponseMessage<T>(
     String message,
     int statusCode,
     T data
) {}
