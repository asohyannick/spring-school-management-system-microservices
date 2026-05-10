package com.ecol.authService.config.securityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.util.UUID;
@Component("userSecurity")
public class UserSecurity {
public boolean isOwner(Authentication authentication, UUID userId) {
	if (authentication == null || !authentication.isAuthenticated()) {
		return false;
	}
	Object principal = authentication.getPrincipal();
	if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
		return authentication.getCredentials() != null &&
				       userId.toString().equals(authentication.getCredentials().toString());
	}
	return false;
}
}