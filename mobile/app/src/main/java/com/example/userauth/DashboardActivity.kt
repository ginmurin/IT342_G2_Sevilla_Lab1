package com.example.userauth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.userauth.api.RetrofitClient
import com.example.userauth.model.LoginResponse
import com.example.userauth.util.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var tvWelcome: TextView
    private lateinit var tvUserInfo: TextView
    private lateinit var tvStatus: TextView
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        sessionManager = SessionManager(this)

        // Check if user is logged in
        if (!sessionManager.isLoggedIn()) {
            navigateToLogin()
            return
        }

        tvWelcome = findViewById(R.id.tvWelcome)
        tvUserInfo = findViewById(R.id.tvUserInfo)
        tvStatus = findViewById(R.id.tvStatus)
        btnLogout = findViewById(R.id.btnLogout)

        // Display welcome message
        val firstName = sessionManager.getFirstName() ?: ""
        val lastName = sessionManager.getLastName() ?: ""
        tvWelcome.text = "Welcome back, $firstName $lastName!"

        // Display user info
        val username = sessionManager.getUsername() ?: ""
        val email = sessionManager.getEmail() ?: ""
        val role = sessionManager.getRole() ?: ""
        tvUserInfo.text = "@$username • $role\nEmail: $email"

        // Display status
        tvStatus.text = "Active\nUser\n100%"

        btnLogout.setOnClickListener {
            performLogout()
        }
    }

    private fun performLogout() {
        val token = sessionManager.getSessionToken() ?: ""
        if (token.isEmpty()) {
            sessionManager.clearSession()
            navigateToLogin()
            return
        }

        btnLogout.isEnabled = false
        btnLogout.text = "Logging out..."

        val call = RetrofitClient.apiService.logout("Bearer $token")

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                sessionManager.clearSession()
                Toast.makeText(
                    this@DashboardActivity,
                    "Logged out successfully",
                    Toast.LENGTH_SHORT
                ).show()
                navigateToLogin()
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Even if logout fails, clear session and navigate to login
                sessionManager.clearSession()
                navigateToLogin()
            }
        })
    }

    private fun navigateToLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
