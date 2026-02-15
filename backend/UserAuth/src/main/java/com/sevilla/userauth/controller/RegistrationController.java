package com.sevilla.userauth.controller;

import com.sevilla.userauth.dto.RegisterRequest;
import com.sevilla.userauth.model.User;
import com.sevilla.userauth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/register")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowedOrigins}")
public class RegistrationController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> showRegistrationForm(@Valid @RequestBody RegisterRequest request) {
        try {
            // Register user
            User user = userService.register(request);
            RegistrationResponse response = new RegistrationResponse(
                    true,
                    "Registration successful",
                    user.getUsername(),
                    user.getEmail()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            RegistrationResponse errorResponse = new RegistrationResponse(
                    false,
                    e.getMessage(),
                    null,
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // Response DTO
    private record RegistrationResponse(
            boolean success,
            String message,
            String username,
            String email
    ) {}
}
