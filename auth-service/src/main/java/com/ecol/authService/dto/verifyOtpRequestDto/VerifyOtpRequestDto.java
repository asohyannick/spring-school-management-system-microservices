package com.ecol.authService.dto.verifyOtpRequestDto;

import jakarta.validation.constraints.NotBlank;

public record VerifyOtpRequestDto(
		
		@NotBlank(message = "OTP code is required")
		String otpCode
) {}