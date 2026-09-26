package com.example.realLife.repository

import com.example.realLife.model.ChatMessage
import org.springframework.data.mongodb.repository.MongoRepository

interface ChatMessageRepository: MongoRepository<ChatMessage, String> {
    fun findByDoubtIdOrderByTimeAsc(doubtId: String): List<ChatMessage>}