package com.example.realLife.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "students")
data class Student(
    @Id
    val id: String? = null,
    var name: String,
    var studentClass: String, // Department, branch, or semester
    var email: String,
    val phone: String,
    var otp: String? = null,
    var otpExpiry: LocalDateTime? = null
)