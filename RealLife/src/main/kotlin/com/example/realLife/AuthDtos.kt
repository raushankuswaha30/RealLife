package com.example.realLife.dto

import com.example.realLife.model.Student

// Request payload for first-time user registration
data class RegisterAndSendOtpRequest(
    val name: String,
    val studentClass: String,
    val email: String,
    val phone: String
)

// Request payload for returning user login
data class LoginSendOtpRequest(
    val phone: String
)

// Request payload for OTP verification
data class VerifyOtpRequest(
    val phone: String,
    val otp: String
)

// Generic authentication response structure
data class AuthResponse(
    val success: Boolean,
    val message: String,
    val otp: String? = null, // Included in response for direct API testing
    val student: Student? = null
)