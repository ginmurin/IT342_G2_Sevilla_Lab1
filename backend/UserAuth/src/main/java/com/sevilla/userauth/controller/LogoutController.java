package com.sevilla.userauth.controller;

import com.sevilla.userauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/logout")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowedOrigins}")
public class LogoutController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        try {
            if (token == null || token.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new LogoutResponse(false, "No token provided"));
            }

            String sessionToken = token.replace("Bearer ", "");
            boolean isLoggedOut = userService.logout(sessionToken);

            if (isLoggedOut) {
                return ResponseEntity.ok(new LogoutResponse(true, "Logout successful"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new LogoutResponse(false, "Invalid session token"));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LogoutResponse(false, "Logout failed: " + e.getMessage()));
        }
    }

    private record LogoutResponse(boolean success, String message) {}
}
