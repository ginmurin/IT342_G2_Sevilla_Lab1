package com.sevilla.userauth.controller;

import com.sevilla.userauth.dto.UserProfileDTO;
import com.sevilla.userauth.model.User;
import com.sevilla.userauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/user/profile")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowedOrigins}")
public class ProfileController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> viewOwnProfile(@PathVariable Integer userId,
                                            @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            // Validate session token (if provided)
            if (token != null && !token.isEmpty()) {
                Optional<User> authenticatedUser = userService.validateSession(token.replace("Bearer ", ""));
                if (authenticatedUser.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(new ErrorResponse("Invalid or expired token"));
                }
                
                // Verify user is accessing their own profile or is admin
                if (!authenticatedUser.get().getUserId().equals(userId) && 
                    !authenticatedUser.get().getRole().equals(User.UserRole.ADMIN)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(new ErrorResponse("Access denied"));
                }
            }

            UserProfileDTO profile = userService.getUserProfile(userId);
            return ResponseEntity.ok(profile);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> loadProfileData(@PathVariable Integer userId,
                                            @RequestBody UserProfileDTO profileDTO,
                                            @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            // Validate session token
            if (token == null || token.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Authorization token required"));
            }

            String sessionToken = token.replace("Bearer ", "");
            Optional<User> authenticatedUser = userService.validateSession(sessionToken);
            
            if (authenticatedUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Invalid or expired token"));
            }

            // Verify user is updating their own profile
            if (!authenticatedUser.get().getUserId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse("Can only update your own profile"));
            }

            User updatedUser = userService.updateProfile(userId, profileDTO);
            UserProfileDTO updatedProfile = new UserProfileDTO(
                    updatedUser.getUserId(),
                    updatedUser.getUsername(),
                    updatedUser.getEmail(),
                    updatedUser.getFirstName(),
                    updatedUser.getLastName(),
                    updatedUser.getRole().toString(),
                    updatedUser.getIsActive(),
                    updatedUser.getCreatedAt(),
                    updatedUser.getUpdatedAt(),
                    updatedUser.getLastLogin(),
                    null
            );
            
            return ResponseEntity.ok(updatedProfile);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    private record ErrorResponse(String message) {}
}
