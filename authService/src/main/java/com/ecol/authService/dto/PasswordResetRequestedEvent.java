package com.ecol.authService.dto;
import java.time.Instant;
public record PasswordResetRequestedEvent(
	 String eventId,
	 String userId,
	 String email,
	 String resetToken,
	 Instant requestedAt,
	 Instant expiredAt
) { }
