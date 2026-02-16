package com.example.userauth.model

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val userId: String? = null,
    val username: String? = null,
    val email: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val sessionToken: String? = null,
    val role: String? = null
)
