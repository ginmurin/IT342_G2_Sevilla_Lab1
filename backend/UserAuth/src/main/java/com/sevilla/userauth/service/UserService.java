package com.sevilla.userauth.service;

import com.sevilla.userauth.dto.*;
import com.sevilla.userauth.model.User;
import com.sevilla.userauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    // Session storage (in-memory for now, use Redis or database in production)
    private static final ConcurrentHashMap<String, SessionInfo> sessions = new ConcurrentHashMap<>();

    /**
     * Register a new user
     */
    public User register(RegisterRequest request) {
        // Validate username doesn't exist
        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Username already exists");
        }

        // Validate email doesn't exist
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        // Create new user
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .role(User.UserRole.USER)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    /**
     * Login user and generate session token
     */
    public LoginResponse login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(request.username());

        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid credentials");
        }

        User user = userOpt.get();

        // Verify password
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!user.getIsActive()) {
            throw new RuntimeException("User account is inactive");
        }

        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Generate session token
        String sessionToken = UUID.randomUUID().toString();
        sessions.put(sessionToken, new SessionInfo(user.getUserId(), LocalDateTime.now().plusHours(24)));

        return new LoginResponse(
                true,
                sessionToken,
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().toString(),
                user.getIsActive(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getLastLogin()
        );
    }

    /**
     * Logout user and invalidate session
     */
    public boolean logout(String sessionToken) {
        // Get session info
        SessionInfo sessionInfo = sessions.get(sessionToken);
        if (sessionInfo == null) {
            return false;
        }
        
        // Remove session
        sessions.remove(sessionToken);
        
        // Set user as inactive
        Optional<User> userOpt = userRepository.findById(sessionInfo.userId());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setIsActive(false);
            userRepository.save(user);
        }
        
        return true;
    }

    /**
     * Get user by ID
     */
    public Optional<User> getUserById(Integer userId) {
        return userRepository.findById(userId);
    }

    /**
     * Get user by username
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Update user profile
     */
    public User updateProfile(Integer userId, UserProfileDTO profileDTO) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();
        
        // Update all fields if provided
        if (profileDTO.username() != null && !profileDTO.username().isEmpty()) {
            user.setUsername(profileDTO.username());
        }
        if (profileDTO.email() != null && !profileDTO.email().isEmpty()) {
            user.setEmail(profileDTO.email());
        }
        if (profileDTO.firstName() != null && !profileDTO.firstName().isEmpty()) {
            user.setFirstName(profileDTO.firstName());
        }
        if (profileDTO.lastName() != null && !profileDTO.lastName().isEmpty()) {
            user.setLastName(profileDTO.lastName());
        }
        if (profileDTO.password() != null && !profileDTO.password().isEmpty()) {
            // Validate password length
            if (profileDTO.password().length() < 6) {
                throw new RuntimeException("Password must be at least 6 characters");
            }
            user.setPasswordHash(passwordEncoder.encode(profileDTO.password()));
        }
        
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    /**
     * Validate session token
     */
    public Optional<User> validateSession(String sessionToken) {
        SessionInfo sessionInfo = sessions.get(sessionToken);
        
        if (sessionInfo == null) {
            return Optional.empty();
        }

        // Check if session is expired
        if (LocalDateTime.now().isAfter(sessionInfo.expiryTime())) {
            sessions.remove(sessionToken);
            return Optional.empty();
        }

        return userRepository.findById(sessionInfo.userId());
    }

    /**
     * Get user profile DTO
     */
    public UserProfileDTO getUserProfile(Integer userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();
        return new UserProfileDTO(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().toString(),
                user.getIsActive(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getLastLogin(),
                null
        );
    }

    // Inner class for session information
    private record SessionInfo(Integer userId, LocalDateTime expiryTime) {}
}
