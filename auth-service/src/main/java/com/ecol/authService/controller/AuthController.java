package com.ecol.authService.controller;
import com.ecol.authService.dto.*;
import com.ecol.authService.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Registration, OTP verification and authentication endpoints")
public class AuthController {

		private final AuthService authService;
		
		@PostMapping("/register")
		@Operation(summary = "Register a new user",
				description = "Creates a new STUDENT account and sends an OTP to the provided email")
		public ResponseEntity<ApiResponse<UserResponseDto>> register(
				@Valid @RequestBody AuthRequestDto request,
				HttpServletResponse response) {
			
			return ResponseEntity
					       .status(HttpStatus.CREATED)
					       .body(authService.register(request, response));
		}
		
		@PostMapping("/verify-otp")
		@Operation(summary = "Verify OTP",
				description = "Verifies the OTP sent to the user's email and activates the account. " +
						              "Access and refresh tokens are set in HttpOnly cookies on success.")
		public ResponseEntity<ApiResponse<UserResponseDto>> verifyOtp(
				@Valid @RequestBody VerifyOtpRequestDto request,
				HttpServletResponse response) {
			
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(authService.verifyOtp(request, response));
		}
		
		@PostMapping("/resend-otp")
		@Operation(summary = "Resend OTP",
				description = "Generates and sends a fresh OTP to the user's email")
		public ResponseEntity<ApiResponse<Void>> resendOtp(
				@RequestParam String email) {
			
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(authService.resendOtp(email));
		}

		@PostMapping("/login")
		@Operation(summary = "Login",
				description = "Authenticates user. Role is read from DB automatically. " +
						              "Tokens set as HttpOnly cookies.")
		public ResponseEntity<ApiResponse<UserResponseDto>> login(
				@Valid @RequestBody LoginRequestDto request,
				HttpServletResponse response) {
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(authService.login(request, response));
		}
		
		@PostMapping("/logout")
		@Operation(summary = "Logout",
				description = "Clears tokens from DB and expires cookies in browser")
		public ResponseEntity<ApiResponse<Void>> logout(
				HttpServletRequest request,
				HttpServletResponse response) {
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(authService.logout(request, response));
		}
		
		@PatchMapping("/admin/block/{userId}")
		@PreAuthorize ("hasRole('ADMIN')")
		@Operation(summary = "Block a user",
				description = "ADMIN only. Blocks user and immediately invalidates their session.")
		public ResponseEntity<ApiResponse<UserResponseDto>> blockUser(
				@PathVariable UUID userId,
				@Valid @RequestBody BlockUserRequestDto request) {
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(authService.blockUser(userId, request));
		}
		
		@PatchMapping("/admin/unblock/{userId}")
		@PreAuthorize("hasRole('ADMIN')")
		@Operation(summary = "Unblock a user",
				description = "ADMIN only. Restores user account access.")
		public ResponseEntity<ApiResponse<UserResponseDto>> unblockUser(
				@PathVariable UUID userId) {
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(authService.unblockUser(userId));
		}

		@GetMapping("/admin/users")
		@PreAuthorize("hasRole('ADMIN')")
		@Operation(
				summary = "Fetch all users",
				description = "ADMIN only. Returns a paginated list of all users. " +
						              "Use ?page=0&size=10&sort=createdAt,desc to control pagination."
		)
		public ResponseEntity<ApiResponse< Page <UserResponseDto> >> fetchAllUsers(
				@PageableDefault (
						page = 0,
						size = 10,
						sort = "createdAt",
						direction = Sort.Direction.DESC
				) Pageable pageable) {
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(authService.fetchAllUsers(pageable));
		}

		@GetMapping("/users/{userId}")
		@PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(authentication, #userId)")
		@Operation(
				summary = "Fetch one user by ID",
				description = "ADMIN can fetch any user. " +
						              "Regular users can only fetch their own profile."
		)
		public ResponseEntity<ApiResponse<UserResponseDto>> fetchOneUser(
				@PathVariable UUID userId) {
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(authService.fetchOneUser(userId));
		}

		@DeleteMapping("/admin/users/{userId}")
		@PreAuthorize("hasRole('ADMIN')")
		@Operation(
				summary = "Delete a user",
				description = "ADMIN only. Soft-deletes the user — data is retained for audit " +
						              "but the account is permanently deactivated and session cleared."
		)
		public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID userId) {
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(authService.deleteUser(userId));
		}

		@PostMapping("/forgot-password")
		@Operation(
				summary = "Forgot password",
				description = "Sends a password reset OTP to the provided email. " +
						              "Always returns the same message whether email exists or not " +
						              "to prevent user enumeration attacks."
		)
		public ResponseEntity<ApiResponse<Void>> forgotPassword(
				@Valid @RequestBody ForgotPasswordRequestDto request) {
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(authService.forgotPassword(request));
		}
		
		@PostMapping("/reset-password")
		@Operation(
				summary = "Reset password",
				description = "Validates OTP and sets a new password. " +
						              "All active sessions are invalidated after a successful reset."
		)
		public ResponseEntity<ApiResponse<Void>> resetPassword(
				@Valid @RequestBody ResetPasswordRequestDto request) {
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(authService.resetPassword(request));
		}
}