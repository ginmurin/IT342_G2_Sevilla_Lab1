package com.sevilla.userauth.dto;

public record LoginResponse(
    boolean isAuthenticated,
    String sessionToken,
    String username,
    String email,
    String firstName,
    String lastName
) {}
