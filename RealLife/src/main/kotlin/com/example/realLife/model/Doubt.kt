package com.example.realLife.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document


@Document(collection = "doubts")
data class Doubt(
    @Id
    val id: String? = null,
    val studentName: String,
    val studentRoll: String,
    val subject: String,
    val topic: String,
    val question: String,
    val pageReference: String = "",
    val urgency: String = "NORMAL",
    var status: String = "OPEN",
    val assignedProfessor: String,
    var professorAnswer: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)