package com.example.userauth.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("isAuthenticated")
    val success: Boolean = false,
    val message: String? = null,
    val userId: Int? = null,
    val username: String? = null,
    val email: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val sessionToken: String? = null,
    val role: String? = null,
    val isActive: Boolean? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val lastLogin: String? = null
)
