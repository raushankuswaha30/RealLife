package com.example.realLife.controller

import com.example.realLife.model.ChatMessage
import com.example.realLife.repository.ChatMessageRepository
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@CrossOrigin(origins = ["*"])
class ChatMessageController(
    private val repository: ChatMessageRepository,
    private val messagingTemplate: SimpMessagingTemplate
) {
    // Handle real-time WebSocket messages and broadcast to subscribers
    @MessageMapping("/chat/{doubtId}")
    fun sendLiveMessage(
        @DestinationVariable doubtId: String,
        chatMessage: ChatMessage
    ) {
        val saveMessage = repository.save(chatMessage)
        messagingTemplate.convertAndSend("/topic/doubt/$doubtId", saveMessage)
    }

    // Retrieve all chat messages (primarily used for testing endpoints)
    @GetMapping("/api/chat")
    @ResponseBody
    fun getAllMessages(): ResponseEntity<List<ChatMessage>> {
        val allMessages = repository.findAll()
        return ResponseEntity.ok(allMessages)
    }

    // Fetch chat history for a specific doubt ordered by timestamp
    @GetMapping("/api/chat/{doubtId}")
    @ResponseBody
    fun getChatMessage(@PathVariable doubtId: String): ResponseEntity<List<ChatMessage>> {
        val history = repository.findByDoubtIdOrderByTimeAsc(doubtId)
        return ResponseEntity.ok(history)
    }

    // Fallback REST endpoint to send messages and broadcast via WebSocket
    @PostMapping("/api/chat/send")
    @ResponseBody
    fun sendRestMessage(@RequestBody chatMessage: ChatMessage): ResponseEntity<ChatMessage> {
        val saved = repository.save(chatMessage)
        messagingTemplate.convertAndSend("/topic/doubt/${chatMessage.doubtId}", saved)
        return ResponseEntity.ok(saved)
    }
}