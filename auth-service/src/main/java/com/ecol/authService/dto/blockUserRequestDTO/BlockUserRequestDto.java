package com.ecol.authService.dto.blockUserRequestDTO;
import jakarta.validation.constraints.NotBlank;
public record BlockUserRequestDto(
		@NotBlank(message = "Reason is required")
		String reason
) {}