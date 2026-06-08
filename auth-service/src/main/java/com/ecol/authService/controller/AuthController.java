package com.ecol.authService.controller;
import com.ecol.authService.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "User Service", description = "Registration, OTP verification and authentication endpoints")
public class AuthController {

		private final AuthService authService;
		
}