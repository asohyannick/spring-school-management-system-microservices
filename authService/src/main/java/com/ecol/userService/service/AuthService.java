package com.ecol.userService.service;
import com.ecol.userService.config.JwtConfig.JWTConfig;
import com.ecol.userService.mapper.UserMapper;
import com.ecol.userService.repository.UserRepository;
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
}
