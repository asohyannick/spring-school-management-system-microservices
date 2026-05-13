package com.ecol.authService.dto.resetPasswordRequestDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequestDto(
		
		@NotBlank(message = "Email is required")
		String email,
		
		@NotBlank(message = "OTP code is required")
		String otpCode,
		
		@NotBlank(message = "New password is required")
		@Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
		@Pattern(
				regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$",
				message = "Password must contain uppercase, lowercase, number, and special character"
		)
		String newPassword,
		
		@NotBlank(message = "Confirm password is required")
		String confirmPassword
) {}