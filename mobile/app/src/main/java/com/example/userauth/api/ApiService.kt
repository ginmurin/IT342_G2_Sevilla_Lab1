package com.example.userauth.api

import com.example.userauth.model.LoginRequest
import com.example.userauth.model.LoginResponse
import com.example.userauth.model.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("auth/register")
    fun register(@Body registerRequest: RegisterRequest): Call<LoginResponse>

    @POST("auth/logout")
    fun logout(@Header("Authorization") token: String): Call<LoginResponse>

    @GET("dashboard")
    fun getDashboard(@Header("Authorization") token: String): Call<Map<String, Any>>

    @GET("user/profile")
    fun getUserProfile(@Header("Authorization") token: String): Call<Map<String, Any>>
}
