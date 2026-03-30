package com.ecol.userService.controller;
import com.ecol.userService.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
@RestController
@RequestMapping("/api/1/auth")
@Tag (name = "Authentication and Authorization Service", description = "Endpoints for registration, login, verification, password reset, and token management.")
@RequiredArgsConstructor
public class AuthController {
private final AuthService authService;
    @GetMapping("/users")
    public List<String> getUsers() {
        return  List.of("John", "Peter", "James");
    }
}
