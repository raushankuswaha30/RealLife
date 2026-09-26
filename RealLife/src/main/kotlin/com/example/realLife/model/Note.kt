package com.example.realLife.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "notes")
data class Note(
    @Id val id: String? = null,
    val title: String = "",
    val subject: String = "",
    val professorName: String = "",
    val pdfLink: String = "",
    val uploadedAt: Long = System.currentTimeMillis()
)