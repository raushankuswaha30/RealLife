package com.example.realLife.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "chat_messages")
data class ChatMessage(
    @Id val id: String? = null,
    val doubtId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val senderRole: String = "STUDENT",
    val message: String = "",
    val time: Long = System.currentTimeMillis() // Repository method 'time' expect kar raha hai
)