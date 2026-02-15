package com.sevilla.userauth.controller;

import com.sevilla.userauth.dto.LoginRequest;
import com.sevilla.userauth.dto.LoginResponse;
import com.sevilla.userauth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/login")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowedOrigins}")
public class LoginController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> showLoginForm(@Valid @RequestBody LoginRequest request) {
        try {
            // Validate credentials
            LoginResponse response = userService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid credentials", false));
        }
    }

    // Error response DTO
    private record ErrorResponse(String message, boolean success) {}
}
