package com.sevilla.userauth.controller;

import com.sevilla.userauth.model.User;
import com.sevilla.userauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowedOrigins}")
public class DashboardController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> displayDashboard(@RequestHeader(value = "Authorization", required = false) String token) {
        try {
            if (token == null || token.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Authorization token required"));
            }

            String sessionToken = token.replace("Bearer ", "");
            Optional<User> user = userService.validateSession(sessionToken);

            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Invalid or expired token"));
            }

            DashboardContent dashboard = new DashboardContent(
                    "Welcome, " + user.get().getFirstName(),
                    "User Dashboard",
                    "You have access to your dashboard"
            );

            return ResponseEntity.ok(dashboard);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    private record DashboardContent(
            String greeting,
            String title,
            String content
    ) {}

    private record ErrorResponse(String message) {}
}
