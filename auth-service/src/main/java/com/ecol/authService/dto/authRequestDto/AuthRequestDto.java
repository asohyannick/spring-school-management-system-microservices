package com.ecol.authService.dto.authRequestDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

import java.time.Instant;

public record AuthRequestDto(
		@NotBlank(message = "First name is required")
		@Size(max = 100, message = "First name must not exceed 100 characters")
		String firstName,
		
		@NotBlank(message = "Last name is required")
		@Size(max = 100, message = "Last name must not exceed 100 characters")
		String lastName,
		
		@NotBlank(message = "Email is required")
		@Email(message = "Email must be a valid email address")
		@Size(max = 150, message = "Email must not exceed 150 characters")
		String email,
		
		@NotBlank(message = "Password is required")
		@Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
		@Pattern(
				regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$",
				message = "Password must contain uppercase, lowercase, number, and special character"
		)
		String password,
		
		Instant registeredAt
) {}