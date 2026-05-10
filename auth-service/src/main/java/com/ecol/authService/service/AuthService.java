package com.ecol.authService.service;
import com.ecol.authService.config.JwtConfig.JWTConfig;
import com.ecol.authService.config.mailConfig.EmailConfigTemplate;
import com.ecol.authService.dto.*;
import com.ecol.authService.entity.User;
import com.ecol.authService.enums.UserRole;
import com.ecol.authService.exception.BadRequestExceptionHandler;
import com.ecol.authService.exception.ConflictExceptionHandler;
import com.ecol.authService.exception.NotFoundExceptionHandler;
import com.ecol.authService.mapper.UserMapper;
import com.ecol.authService.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
		 private  final UserRepository userRepository;
		 private final JWTConfig jwtConfig;
		 private  final PasswordEncoder passwordEncoder;
		 private final UserMapper userMapper;
		 private final EmailConfigTemplate emailConfigTemplate;
		 private final AuthEventProducer authEventProducer;
		private static final long   OTP_EXPIRY_MINUTES          = 5;
		private static final long   MAGIC_LINK_EXPIRY_MINUTES   = 15;
		private static final int    MAX_FAILED_ATTEMPTS         = 5;
		private static final long   LOCK_DURATION_MINUTES       = 30;
		private static final String OTP_PREFIX                  = "OTP:";
		private static final String ACCESS_TOKEN_COOKIE         = "accessToken";
		private static final String REFRESH_TOKEN_COOKIE        = "refreshToken";
		
		
		private User findByEmail( String email) {
			return userRepository.findByEmail(email)
					       .orElseThrow(() -> new NotFoundExceptionHandler ("User not found with email: " + email));
		}

		private User findById(UUID userId) {
			return userRepository.findById(userId)
					       .orElseThrow(() -> new NotFoundExceptionHandler(
							       "User not found with id: " + userId
					       ));
		}

		private void handleFailedLogin(User user) {
			int attempts = user.getFailedLoginAttempts() + 1;
			user.setFailedLoginAttempts(attempts);
			if (attempts >= MAX_FAILED_ATTEMPTS) {
				user.setLockedUntil(Instant.now().plus(Duration.ofMinutes(LOCK_DURATION_MINUTES)));
				user.setAccountLocked(true);
				log.warn("🔒 Account locked: {} after {} failed attempts",
						user.getEmail(), attempts);
			}
			userRepository.save(user);
		}
		
		private void setTokenCookies( HttpServletResponse response, String accessToken, String refreshToken) {
			Cookie accessCookie = new Cookie(ACCESS_TOKEN_COOKIE, accessToken);
			accessCookie.setHttpOnly(true);
			accessCookie.setSecure(true);
			accessCookie.setPath("/");
			accessCookie.setMaxAge((int) jwtConfig.getAccessTokenExpirationSeconds());
			
			Cookie refreshCookie = new Cookie(REFRESH_TOKEN_COOKIE, refreshToken);
			refreshCookie.setHttpOnly(true);
			refreshCookie.setSecure(true);
			refreshCookie.setPath("/");
			refreshCookie.setMaxAge((int) jwtConfig.getRefreshTokenExpirationSeconds());
			
			response.addCookie(accessCookie);
			response.addCookie(refreshCookie);
		}
		
		private void clearTokenCookies(HttpServletResponse response) {
			Cookie accessCookie = new Cookie(ACCESS_TOKEN_COOKIE, null);
			accessCookie.setHttpOnly(true);
			accessCookie.setSecure(true);
			accessCookie.setPath("/");
			accessCookie.setMaxAge(0);
			
			Cookie refreshCookie = new Cookie(REFRESH_TOKEN_COOKIE, null);
			refreshCookie.setHttpOnly(true);
			refreshCookie.setSecure(true);
			refreshCookie.setPath("/");
			refreshCookie.setMaxAge(0);
			
			response.addCookie(accessCookie);
			response.addCookie(refreshCookie);
		}
		
		private String extractTokenFromCookie( HttpServletRequest request, String cookieName) {
			if (request.getCookies() == null) return null;
			for ( Cookie cookie : request.getCookies()) {
				if (cookieName.equals(cookie.getName())) return cookie.getValue();
			}
			return null;
		}
		
		private String extractFirstName(String fullName) {
			if (fullName == null || fullName.isBlank()) return "Google";
			String[] parts = fullName.trim().split(" ");
			return parts[0];
		}
		
		private String extractLastName(String fullName) {
			if (fullName == null || fullName.isBlank()) return "User";
			String[] parts = fullName.trim().split(" ", 2);
			return parts.length > 1 ? parts[1] : "";
		}
		
		

	@Transactional
	public ApiResponse< UserResponseDto > register(
			AuthRequestDto request,
			HttpServletResponse response) {
		
		if (userRepository.findByEmail(request.email().toLowerCase()).isPresent()) {
			throw new ConflictExceptionHandler(
					"An account with email " + request.email() + " already exists"
			);
		}
		
		User user = userMapper.toEntity(request);
		
		user.setPassword(passwordEncoder.encode(request.password()));
		
		user.setRole( UserRole.STUDENT);
		
		String otpCode = emailConfigTemplate.generateOtp();
		user.setOtpCode(otpCode);
		user.setOtpExpiryDate(Instant.now().plus(Duration.ofMinutes(OTP_EXPIRY_MINUTES)));
		user.setOtpCodeVerified(false);
		user.setAccountVerified(false);
		
		user.setMagicLinkExpiryDate(Instant.now());
		
		User savedUser = userRepository.save(user);
		log.info("✅ New user registered: {} | role: STUDENT | verified: false",
				savedUser.getEmail());
		
		emailConfigTemplate.sendOtpEmail(
				savedUser.getEmail(),
				savedUser.getFirstName(),
				otpCode
		);
		log.info("📧 OTP email sent to: {}", savedUser.getEmail());
		
		UserRegisteredEvent event = new UserRegisteredEvent(
				java.util.UUID.randomUUID().toString(),
				savedUser.getId().toString(),
				savedUser.getFirstName(),
				savedUser.getLastName(),
				savedUser.getEmail(),
				savedUser.getRole().name(),
				savedUser.getCreatedAt()
		);
		authEventProducer.publishUserRegistered(event);
		
		return ApiResponse.success(
				"Registration successful! Please check your email for the OTP verification code.",
				userMapper.toResponseDto(savedUser)
		);
	}

	@Transactional
	public ApiResponse<UserResponseDto> verifyOtp(
			VerifyOtpRequestDto request,
			HttpServletResponse response) {
		
		User user = findByEmail(request.email());
		
		if (user.isAccountVerified()) {
			throw new BadRequestExceptionHandler("Account is already verified");
		}
		
		if (user.getOtpExpiryDate() == null ||
				    Instant.now().isAfter(user.getOtpExpiryDate())) {
			throw new BadRequestExceptionHandler(
					"OTP has expired. Please request a new one."
			);
		}
		
		if (!request.otpCode().equals(user.getOtpCode())) {
			throw new BadRequestExceptionHandler("Invalid OTP code. Please try again.");
		}
		
		user.setAccountVerified(true);
		user.setOtpCodeVerified(true);
		user.setOtpCode(null);
		user.setOtpExpiryDate(null);
		
		String accessToken  = jwtConfig.generateAccessToken(
			user.getEmail(),
				user.getRole().name()
		);
		String refreshToken = jwtConfig.generateRefreshToken(
				user.getEmail()
		);
		
		user.setAccessToken(accessToken);
		user.setRefreshToken(refreshToken);
		
		User verifiedUser = userRepository.save(user);
		log.info("✅ Account verified for: {}", verifiedUser.getEmail());
		
		setTokenCookies(response, accessToken, refreshToken);
		
		return ApiResponse.success(
				"Account verified successfully! You are now logged in.",
				userMapper.toResponseDto(verifiedUser)
		);
	}

	@Transactional
	public ApiResponse<Void> resendOtp(String email) {
		User user = findByEmail(email);
		
		if (user.isAccountVerified()) {
			throw new BadRequestExceptionHandler("Account is already verified");
		}
		
		String newOtp = emailConfigTemplate.generateOtp();
		user.setOtpCode(newOtp);
		user.setOtpExpiryDate(Instant.now().plus(Duration.ofMinutes(OTP_EXPIRY_MINUTES)));
		userRepository.save(user);
		
		emailConfigTemplate.sendOtpEmail(user.getEmail(), user.getFirstName(), newOtp);
		log.info("📧 OTP resent to: {}", user.getEmail());
		
		return ApiResponse.success("A new OTP has been sent to " + email);
	}

	@Transactional
	public ApiResponse<UserResponseDto> login(
			LoginRequestDto request,
			HttpServletResponse response) {
		
		User user = findByEmail(request.email());
		
		if (user.isAccountDeleted()) {
			throw new BadRequestExceptionHandler(
					"This account has been deleted. Please contact support."
			);
		}
		
		if (user.isAccountBlocked()) {
			throw new BadRequestExceptionHandler(
					"Your account has been blocked. Please contact the administrator."
			);
		}
		
		if (user.isAccountSuspended()) {
			throw new BadRequestExceptionHandler(
					"Your account is suspended. Please contact support."
			);
		}
		
		if (!user.isAccountVerified()) {
			throw new BadRequestExceptionHandler(
					"Please verify your email first. Check your inbox for the OTP."
			);
		}
		
		if (user.getLockedUntil() != null &&
				    Instant.now().isBefore(user.getLockedUntil())) {
			long minutesLeft = Duration.between(Instant.now(), user.getLockedUntil())
					                   .toMinutes() + 1;
			throw new BadRequestExceptionHandler(
					"Account is temporarily locked. Try again in " + minutesLeft + " minute(s)."
			);
		}
		
		if (user.getLockedUntil() != null &&
				    Instant.now().isAfter(user.getLockedUntil())) {
			user.setLockedUntil(null);
			user.setAccountLocked(false);
			user.setFailedLoginAttempts(0);
			log.info("🔓 Account auto-unlocked for: {}", user.getEmail());
		}
		
		if (!passwordEncoder.matches(request.password(), user.getPassword())) {
			handleFailedLogin(user);
			int remaining = MAX_FAILED_ATTEMPTS - user.getFailedLoginAttempts();
			String message = remaining > 0
					                 ? "Invalid password. " + remaining + " attempt(s) remaining before lockout."
					                 : "Invalid password. Your account has been locked for " +
							                   LOCK_DURATION_MINUTES + " minutes.";
			throw new BadRequestExceptionHandler(message);
		}
		
		user.setFailedLoginAttempts(0);
		user.setLockedUntil(null);
		user.setAccountLocked(false);
		
		String accessToken = jwtConfig.generateAccessToken(
				user.getEmail(),
				user.getRole().name()
		);
		String refreshToken = jwtConfig.generateRefreshToken(
				user.getEmail()
		);
		
		user.setAccessToken(accessToken);
		user.setRefreshToken(refreshToken);
		User loggedInUser = userRepository.save(user);
		
		log.info("✅ User logged in: {} | role: {}", loggedInUser.getEmail(),
				loggedInUser.getRole());
		
		setTokenCookies(response, accessToken, refreshToken);
		
		return ApiResponse.success(
				"Login successful! Welcome back, " + loggedInUser.getFirstName() + ".",
				userMapper.toResponseDto(loggedInUser)
		);
	}

	@Transactional
	public ApiResponse<Void> logout(
			HttpServletRequest request,
			HttpServletResponse response) {
			
			String refreshToken = extractTokenFromCookie(request, REFRESH_TOKEN_COOKIE);
		
		if (refreshToken != null) {
			userRepository.findByRefreshToken(refreshToken).ifPresent(user -> {
				user.setAccessToken(null);
				user.setRefreshToken(null);
				userRepository.save(user);
				log.info("✅ User logged out: {}", user.getEmail());
			});
		}
		
		clearTokenCookies(response);
		
		return ApiResponse.success("Logged out successfully.");
	}

	@Transactional
	public ApiResponse<UserResponseDto> blockUser( UUID userId, BlockUserRequestDto request) {
		
		User user = findById(userId);
		
		if (user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.SUPER_ADMIN) {
			throw new BadRequestExceptionHandler("Admin or Super Admin accounts cannot be blocked.");
		}
		
		if (user.isAccountBlocked()) {
			throw new BadRequestExceptionHandler(
					"User " + user.getEmail() + " is already blocked."
			);
		}
		
		user.setAccountBlocked(true);
		user.setAccessToken(null);
		user.setRefreshToken(null);
		
		User blockedUser = userRepository.save(user);
		log.warn("🚫 User blocked: {} | reason: {}", blockedUser.getEmail(), request.reason());
		
		authEventProducer.publishAccountLocked(
				blockedUser.getId().toString(),
				blockedUser.getEmail(),
				request.reason()
		);
		
		return ApiResponse.success(
				"User " + blockedUser.getEmail() + " has been blocked successfully.",
				userMapper.toResponseDto(blockedUser)
		);
	}

	@Transactional
	public ApiResponse<UserResponseDto> unblockUser(UUID userId) {
		
		User user = findById(userId);
		
		if (!user.isAccountBlocked()) {
			throw new BadRequestExceptionHandler(
					"User " + user.getEmail() + " is not currently blocked."
			);
		}
		
		user.setAccountBlocked(false);
		user.setFailedLoginAttempts(0);
		user.setLockedUntil(null);
		user.setAccountLocked(false);
		
		User unblockedUser = userRepository.save(user);
		log.info("✅ User unblocked: {}", unblockedUser.getEmail());
		
		return ApiResponse.success(
				"User " + unblockedUser.getEmail() + " has been unblocked successfully.",
				userMapper.toResponseDto(unblockedUser)
		);
	}

	@Transactional(readOnly = true)
	public ApiResponse< Page <UserResponseDto> > fetchAllUsers( Pageable pageable) {
		Page<UserResponseDto> users = userRepository
				                              .findAll(pageable)
				                              .map(userMapper::toResponseDto);
		
		log.info("📋 Fetched {} users (page {}/{})",
				users.getNumberOfElements(),
				pageable.getPageNumber(),
				users.getTotalPages());
		
		return ApiResponse.success(
				"Users fetched successfully. Total: " + users.getTotalElements(),
				users
		);
	}
	
	@Transactional(readOnly = true)
	public ApiResponse<UserResponseDto> fetchOneUser(UUID userId) {
		User user = findById(userId);
		
		log.info("🔍 Fetched user: {} | role: {}", user.getEmail(), user.getRole());
		
		return ApiResponse.success(
				"User fetched successfully.",
				userMapper.toResponseDto(user)
		);
	}

	@Transactional
	public ApiResponse<Void> deleteUser(UUID userId) {
		User user = findById(userId);
		
		if (user.isAccountDeleted()) {
			throw new BadRequestExceptionHandler(
					"User " + user.getEmail() + " has already been deleted."
			);
		}
		
		if (user.getRole() == UserRole.ADMIN) {
			throw new BadRequestExceptionHandler(
					"Admin accounts cannot be deleted through this endpoint. " +
							"Please contact a super administrator."
			);
		}
		
		user.setAccountDeleted(true);
		user.setAccessToken(null);
		user.setRefreshToken(null);
		
		userRepository.save(user);
		log.warn("🗑️ User soft-deleted: {} | id: {}", user.getEmail(), userId);
		
		return ApiResponse.success(
				"User " + user.getEmail() + " has been deleted successfully."
		);
	}

	@Transactional
	public ApiResponse<Void> forgotPassword(ForgotPasswordRequestDto request) {
		
		var optionalUser = userRepository.findByEmail(request.email().toLowerCase());
		
		if (optionalUser.isEmpty()) {
			log.warn("⚠️ Forgot password requested for non-existent email: {}",
					request.email());
			return ApiResponse.success(
					"If an account exists with this email, a password reset OTP has been sent."
			);
		}
		
		User user = optionalUser.get();
		
		if (user.isAccountDeleted()) {
			log.warn("⚠️ Forgot password attempted on deleted account: {}",
					user.getEmail());
			return ApiResponse.success(
					"If an account exists with this email, a password reset OTP has been sent."
			);
		}
		
		if (user.isAccountBlocked()) {
			throw new IllegalStateException(
					"Your account has been blocked. Please contact the administrator."
			);
		}
		
		String resetOtp = emailConfigTemplate.generateOtp();
		
		user.setForgotPassword(resetOtp);
		user.setOtpExpiryDate(Instant.now().plus(Duration.ofMinutes(OTP_EXPIRY_MINUTES)));
		userRepository.save(user);
		
		emailConfigTemplate.sendPasswordResetOtpEmail(
				user.getEmail(),
				user.getFirstName(),
				resetOtp
		);
		log.info("📧 Password reset OTP sent to: {}", user.getEmail());
		
		authEventProducer.publishPasswordResetRequested(
				new PasswordResetRequestedEvent(
						UUID.randomUUID().toString(),
						user.getId().toString(),
						user.getEmail(),
						resetOtp,
						Instant.now(),
						user.getOtpExpiryDate()
				)
		);
		
		return ApiResponse.success(
				"If an account exists with this email, a password reset OTP has been sent."
		);
	}

	@Transactional
	public ApiResponse<Void> resetPassword(ResetPasswordRequestDto request) {
		
		User user = findByEmail(request.email());
		
		if (user.getForgotPassword() == null) {
			throw new BadRequestExceptionHandler(
					"No password reset was requested for this account. " +
							"Please use the forgot password flow first."
			);
		}
		
		if (!request.otpCode().equals(user.getForgotPassword())) {
			throw new BadRequestExceptionHandler(
					"Invalid OTP code. Please check your email and try again."
			);
		}
		
		if (user.getOtpExpiryDate() == null ||
				    Instant.now().isAfter(user.getOtpExpiryDate())) {
			user.setForgotPassword(null);
			user.setOtpExpiryDate(null);
			userRepository.save(user);
			throw new BadRequestExceptionHandler(
					"OTP has expired. Please request a new password reset."
			);
		}
		
		if (!request.newPassword().equals(request.confirmPassword())) {
			throw new BadRequestExceptionHandler(
					"New password and confirm password do not match."
			);
		}
		
		if (passwordEncoder.matches(request.newPassword(), user.getPassword())) {
			throw new BadRequestExceptionHandler(
					"New password must be different from your current password."
			);
		}
		
		user.setPassword(passwordEncoder.encode(request.newPassword()));
		
		user.setForgotPassword(null);
		user.setResetPassword(null);
		user.setOtpExpiryDate(null);
		user.setOtpCode(null);
		
		user.setAccessToken(null);
		user.setRefreshToken(null);
		
		user.setFailedLoginAttempts(0);
		user.setLockedUntil(null);
		user.setAccountLocked(false);
		
		userRepository.save(user);
		log.info("✅ Password reset successfully for: {}", user.getEmail());
		emailConfigTemplate.sendPasswordChangedConfirmationEmail(
				user.getEmail(),
				user.getFirstName()
		);
		return ApiResponse.success(
				"Password reset successfully. Please log in with your new password."
		);
	}
	
}
