package com.sevilla.userauth.controller;

import com.sevilla.userauth.model.User;
import com.sevilla.userauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/protected")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowedOrigins}")
public class ProtectedPagesController {

    private final UserService userService;

    @GetMapping("/pages")
    public ResponseEntity<?> displayProtectedPages(@RequestHeader(value = "Authorization", required = false) String token) {
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

            // Check if user is logged in
            if (!user.get().getIsActive()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse("User account is inactive"));
            }

            ProtectedPageContent content = new ProtectedPageContent(
                    true,
                    "Access Granted",
                    "You are accessing protected content",
                    user.get().getUsername()
            );

            return ResponseEntity.ok(content);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @CheckLoginStatus
    @GetMapping("/check-status")
    public ResponseEntity<?> checkLoginStatus(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token != null && !token.isEmpty()) {
            String sessionToken = token.replace("Bearer ", "");
            Optional<User> user = userService.validateSession(sessionToken);
            
            if (user.isPresent()) {
                return ResponseEntity.ok(new LoginStatus(true, user.get().getUsername()));
            }
        }
        
        return ResponseEntity.ok(new LoginStatus(false, null));
    }

    private record ProtectedPageContent(
            boolean hasAccess,
            String status,
            String message,
            String username
    ) {}

    private record ErrorResponse(String message) {}

    private record LoginStatus(boolean loggedIn, String username) {}

    @interface CheckLoginStatus {}
}
