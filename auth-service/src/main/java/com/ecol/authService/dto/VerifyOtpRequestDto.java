package com.ecol.authService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyOtpRequestDto(
		
		@NotBlank(message = "Email is required")
		@Email(message = "Must be a valid email")
		String email,
		
		@NotBlank(message = "OTP code is required")
		String otpCode
) {}