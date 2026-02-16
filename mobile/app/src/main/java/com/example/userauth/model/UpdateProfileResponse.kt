package com.example.userauth.model

data class UpdateProfileResponse(
    val userId: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: String,
    val isActive: Boolean,
    val createdAt: String?,
    val updatedAt: String?,
    val lastLogin: String?,
    val password: String?
)
