package com.example.userauth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.userauth.api.RetrofitClient
import com.example.userauth.model.RegisterRequest
import com.example.userauth.model.LoginResponse
import com.example.userauth.util.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var inputUsername: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputFirstName: EditText
    private lateinit var inputLastName: EditText
    private lateinit var inputPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        sessionManager = SessionManager(this)

        inputUsername = findViewById(R.id.inputUsername)
        inputEmail = findViewById(R.id.inputEmail)
        inputFirstName = findViewById(R.id.inputFirstName)
        inputLastName = findViewById(R.id.inputLastName)
        inputPassword = findViewById(R.id.inputPassword)
        btnRegister = findViewById(R.id.btnRegisterSubmit)
        btnLogin = findViewById(R.id.btnLoginLink)

        btnRegister.setOnClickListener {
            performRegister()
        }

        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun performRegister() {
        val username = inputUsername.text.toString().trim()
        val email = inputEmail.text.toString().trim()
        val firstName = inputFirstName.text.toString().trim()
        val lastName = inputLastName.text.toString().trim()
        val password = inputPassword.text.toString().trim()

        if (username.isEmpty() || email.isEmpty() || firstName.isEmpty() || 
            lastName.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (username.length < 3) {
            Toast.makeText(this, "Username must be at least 3 characters", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return
        }

        btnRegister.isEnabled = false
        btnRegister.text = "Registering..."

        val registerRequest = RegisterRequest(username, email, firstName, lastName, password)
        val call = RetrofitClient.apiService.register(registerRequest)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                btnRegister.isEnabled = true
                btnRegister.text = "Register"

                if (response.isSuccessful && response.body() != null) {
                    val registerResponse = response.body()!!
                    if (registerResponse.success) {
                        // Save session
                        sessionManager.saveSession(
                            sessionToken = registerResponse.sessionToken ?: "",
                            userId = registerResponse.userId?.toString() ?: "",
                            username = registerResponse.username ?: "",
                            email = registerResponse.email ?: "",
                            firstName = registerResponse.firstName ?: "",
                            lastName = registerResponse.lastName ?: "",
                            role = registerResponse.role ?: ""
                        )

                        Toast.makeText(
                            this@RegisterActivity,
                            "Registration successful",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this@RegisterActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val errorMsg = if (registerResponse.message.isNullOrEmpty()) "Registration failed" else registerResponse.message
                        Toast.makeText(this@RegisterActivity, errorMsg, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@RegisterActivity, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                btnRegister.isEnabled = true
                btnRegister.text = "Register"
                Toast.makeText(
                    this@RegisterActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
