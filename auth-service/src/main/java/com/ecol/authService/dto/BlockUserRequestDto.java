package com.ecol.authService.dto;
import jakarta.validation.constraints.NotBlank;
public record BlockUserRequestDto(
		@NotBlank(message = "Reason is required")
		String reason
) {}