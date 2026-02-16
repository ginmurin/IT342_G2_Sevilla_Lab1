package com.example.userauth.model

data class User(
    val userId: String,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: String,
    val isActive: Boolean
)
