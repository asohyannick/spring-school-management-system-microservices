package com.ecol.authService.dto.passwordResetRequestedEvent;

import java.time.Instant;
public record PasswordResetRequestedEvent(
		String eventId,
		String userId,
		String email,
		String resetToken,
		Instant requestedAt,
		Instant expiredAt
) { }
