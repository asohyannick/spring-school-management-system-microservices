package com.ecol.authService.service;
import com.ecol.authService.config.JwtConfig.JWTConfig;
import com.ecol.authService.config.mailConfig.EmailConfigTemplate;
import com.ecol.authService.mapper.UserMapper;
import com.ecol.authService.repository.UserRepository;
import com.ecol.authService.utils.AuthEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
		
		

	
}
