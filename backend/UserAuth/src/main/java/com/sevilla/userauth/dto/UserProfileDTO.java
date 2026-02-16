package com.sevilla.userauth.dto;

import java.time.LocalDateTime;

public record UserProfileDTO(
    Integer userId,
    String username,
    String email,
    String firstName,
    String lastName,
    String role,
    Boolean isActive,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime lastLogin,
    String password
) {}
