package com.sevilla.userauth.dto;

import java.time.LocalDateTime;

public record LoginResponse(
    boolean isAuthenticated,
    String sessionToken,
    Integer userId,
    String username,
    String email,
    String firstName,
    String lastName,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime lastLogin
) {}
