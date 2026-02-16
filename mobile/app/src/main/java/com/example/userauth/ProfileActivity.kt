package com.example.userauth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.userauth.api.RetrofitClient
import com.example.userauth.model.UpdateProfileRequest
import com.example.userauth.model.UpdateProfileResponse
import com.example.userauth.util.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var tvProfileName: TextView
    private lateinit var tvProfileUsername: TextView
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var tvFirstName: TextView
    private lateinit var tvLastName: TextView
    private lateinit var etPassword: EditText
    private lateinit var tvRole: TextView
    private lateinit var tvAccountStatus: TextView
    private lateinit var btnEdit: Button
    private lateinit var btnBack: Button
    private lateinit var btnLogout: Button
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button
    private lateinit var passwordSection: LinearLayout
    private lateinit var passwordDivider: View
    private lateinit var saveButtonsLayout: LinearLayout

    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        sessionManager = SessionManager(this)

        // Check if user is logged in
        if (!sessionManager.isLoggedIn()) {
            navigateToLogin()
            return
        }

        initializeViews()
        loadProfileData()
    }

    private fun initializeViews() {
        tvProfileName = findViewById(R.id.tvProfileName)
        tvProfileUsername = findViewById(R.id.tvProfileUsername)
        etUsername = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)
        tvFirstName = findViewById(R.id.tvFirstName)
        tvLastName = findViewById(R.id.tvLastName)
        etPassword = findViewById(R.id.etPassword)
        tvRole = findViewById(R.id.tvRole)
        tvAccountStatus = findViewById(R.id.tvAccountStatus)
        btnEdit = findViewById(R.id.btnEditProfile)
        btnBack = findViewById(R.id.btnBackToDashboard)
        btnLogout = findViewById(R.id.btnLogoutFromProfile)
        btnSave = findViewById(R.id.btnSaveProfile)
        btnCancel = findViewById(R.id.btnCancelEdit)
        passwordSection = findViewById(R.id.passwordSection)
        passwordDivider = findViewById(R.id.passwordDivider)
        saveButtonsLayout = findViewById(R.id.saveButtonsLayout)

        btnEdit.setOnClickListener {
            toggleEditMode(true)
        }

        btnBack.setOnClickListener {
            finish()
        }

        btnLogout.setOnClickListener {
            sessionManager.clearSession()
            navigateToLogin()
        }

        btnSave.setOnClickListener {
            saveProfile()
        }

        btnCancel.setOnClickListener {
            loadProfileData()
            toggleEditMode(false)
        }
    }

    private fun toggleEditMode(enabled: Boolean) {
        isEditMode = enabled

        // Enable/disable input fields (only username, email, password are editable)
        etUsername.isEnabled = enabled
        etEmail.isEnabled = enabled

        // Set background for enabled state
        val backgroundRes = if (enabled) android.R.drawable.edit_text else android.R.color.transparent
        etUsername.setBackgroundResource(backgroundRes)
        etEmail.setBackgroundResource(backgroundRes)

        // Show/hide password section and buttons
        passwordSection.visibility = if (enabled) View.VISIBLE else View.GONE
        passwordDivider.visibility = if (enabled) View.VISIBLE else View.GONE
        saveButtonsLayout.visibility = if (enabled) View.VISIBLE else View.GONE
        btnEdit.visibility = if (enabled) View.GONE else View.VISIBLE
    }

    private fun loadProfileData() {
        val firstName = sessionManager.getFirstName() ?: ""
        val lastName = sessionManager.getLastName() ?: ""
        val username = sessionManager.getUsername() ?: ""
        val email = sessionManager.getEmail() ?: ""
        val role = sessionManager.getRole() ?: "User"

        // Profile header
        val fullName = "$firstName $lastName".trim()
        tvProfileName.text = if (fullName.isNotEmpty()) fullName else "User"
        tvProfileUsername.text = "@$username"

        // Profile information
        etUsername.setText(username)
        etEmail.setText(email)
        tvFirstName.text = firstName.ifEmpty { "N/A" }
        tvLastName.text = lastName.ifEmpty { "N/A" }
        etPassword.setText("")
        tvRole.text = role.replaceFirstChar { it.uppercase() }
        tvAccountStatus.text = "Active"
    }

    private fun saveProfile() {
        val username = etUsername.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()
        
        // Get current firstName and lastName (not editable)
        val firstName = sessionManager.getFirstName() ?: ""
        val lastName = sessionManager.getLastName() ?: ""

        // Validation
        if (username.isEmpty()) {
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.isNotEmpty() && password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return
        }

        // Prepare update request
        val updateRequest = UpdateProfileRequest(
            userId = sessionManager.getUserId()?.toIntOrNull(),
            username = username,
            email = email,
            firstName = firstName,
            lastName = lastName,
            role = sessionManager.getRole(),
            isActive = true,
            createdAt = null,
            updatedAt = null,
            lastLogin = null,
            password = password.ifEmpty { null }
        )

        val sessionToken = sessionManager.getSessionToken() ?: ""
        val userId = sessionManager.getUserId() ?: ""
        val apiService = RetrofitClient.apiService

        // Make API call
        apiService.updateProfile(userId, "Bearer $sessionToken", updateRequest)
            .enqueue(object : Callback<UpdateProfileResponse> {
                override fun onResponse(
                    call: Call<UpdateProfileResponse>,
                    response: Response<UpdateProfileResponse>
                ) {
                    if (response.isSuccessful) {
                        val updateResponse = response.body()
                        if (updateResponse != null) {
                            // Update session with new data
                            val currentToken = sessionManager.getSessionToken() ?: ""
                            sessionManager.saveSession(
                                sessionToken = currentToken,
                                userId = updateResponse.userId.toString(),
                                username = updateResponse.username,
                                email = updateResponse.email,
                                firstName = updateResponse.firstName,
                                lastName = updateResponse.lastName,
                                role = updateResponse.role
                            )

                            Toast.makeText(
                                this@ProfileActivity,
                                "Profile updated successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                            loadProfileData()
                            toggleEditMode(false)
                        } else {
                            Toast.makeText(this@ProfileActivity, "Update failed", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(
                            this@ProfileActivity,
                            "Failed to update profile",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                    Toast.makeText(
                        this@ProfileActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun navigateToLogin() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
