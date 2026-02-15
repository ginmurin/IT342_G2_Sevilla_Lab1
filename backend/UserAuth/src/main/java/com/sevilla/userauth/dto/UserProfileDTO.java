package com.sevilla.userauth.dto;

public record UserProfileDTO(
    Integer userId,
    String username,
    String email,
    String firstName,
    String lastName,
    String role,
    Boolean isActive
) {}
