package com.example.realLife.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("faculty")
data class FacultyProfile(
    @Id val id: String? = null,
    val facultyCode: String = "",
    val name: String,
    val title: String,
    val department: String,
    val email: String = "",
    val officeHours: String = "Contact faculty for availability",
    val rating: Double = 4.9,
    val isAvailable: Boolean = true
)