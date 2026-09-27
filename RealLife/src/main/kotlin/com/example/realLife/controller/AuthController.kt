package com.example.realLife.controller

import com.example.realLife.dto.AuthResponse
import com.example.realLife.dto.LoginSendOtpRequest
import com.example.realLife.dto.RegisterAndSendOtpRequest
import com.example.realLife.dto.VerifyOtpRequest
import com.example.realLife.model.Student
import com.example.realLife.repository.StudentRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import kotlin.random.Random

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = ["*"])
class AuthController(
    private val studentRepository: StudentRepository
) {

    // Helper method to generate a random 6-digit OTP
    private fun generateSixDigitOtp(): String = (100000 + Random.nextInt(900000)).toString()

    // 1. Initial registration: captures profile data and generates initial OTP
    @PostMapping("/register-otp")
    fun registerAndSendOtp(@RequestBody request: RegisterAndSendOtpRequest): ResponseEntity<AuthResponse> {
        val existingByEmail = studentRepository.findByEmail(request.email)
        val existingByPhone = studentRepository.findByPhone(request.phone)

        // Prevent linking an existing email to a different phone number
        if (existingByEmail != null && existingByEmail.phone != request.phone) {
            return ResponseEntity.badRequest().body(
                AuthResponse(success = false, message = "Email already registered with another phone number!")
            )
        }

        val otp = generateSixDigitOtp()
        val expiry = LocalDateTime.now().plusMinutes(5)

        // Update profile if phone already exists, otherwise create a new student entity
        val studentToSave = existingByPhone?.apply {
            this.name = request.name
            this.studentClass = request.studentClass
            this.email = request.email
            this.otp = otp
            this.otpExpiry = expiry
        } ?: Student(
            name = request.name,
            studentClass = request.studentClass,
            email = request.email,
            phone = request.phone,
            otp = otp,
            otpExpiry = expiry
        )

        studentRepository.save(studentToSave)
        println("Generated OTP for ${request.phone}: $otp")

        return ResponseEntity.ok(
            AuthResponse(
                success = true,
                message = "Registration details saved. OTP sent successfully.",
                otp = otp
            )
        )
    }

    // 2. Returning student login: verifies phone exists and sends new OTP
    @PostMapping("/login-otp")
    fun loginSendOtp(@RequestBody request: LoginSendOtpRequest): ResponseEntity<AuthResponse> {
        val student = studentRepository.findByPhone(request.phone)
            ?: return ResponseEntity.badRequest().body(
                AuthResponse(success = false, message = "Phone number not registered. Please register first.")
            )

        val otp = generateSixDigitOtp()
        student.otp = otp
        student.otpExpiry = LocalDateTime.now().plusMinutes(5)
        studentRepository.save(student)

        println("Login OTP for ${request.phone}: $otp")

        return ResponseEntity.ok(
            AuthResponse(
                success = true,
                message = "OTP sent successfully.",
                otp = otp
            )
        )
    }

    // 3. OTP verification: validates token, invalidates single-use OTP, and returns full profile
    @PostMapping("/verify-otp")
    fun verifyOtp(@RequestBody request: VerifyOtpRequest): ResponseEntity<AuthResponse> {
        val student = studentRepository.findByPhone(request.phone)
            ?: return ResponseEntity.badRequest().body(
                AuthResponse(success = false, message = "Student record not found.")
            )

        // Check if OTP matches
        if (student.otp == null || student.otp != request.otp) {
            return ResponseEntity.badRequest().body(
                AuthResponse(success = false, message = "Invalid OTP entered.")
            )
        }

        // Check if OTP has expired
        if (student.otpExpiry == null || student.otpExpiry!!.isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(
                AuthResponse(success = false, message = "OTP has expired. Please request a new one.")
            )
        }

        // Clear OTP fields to prevent replay attacks
        student.otp = null
        student.otpExpiry = null
        val verifiedStudent = studentRepository.save(student)

        return ResponseEntity.ok(
            AuthResponse(
                success = true,
                message = "Login successful.",
                student = verifiedStudent
            )
        )
    }
}