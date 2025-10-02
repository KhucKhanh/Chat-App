package com.example.chatapp.Data

data class Message(
    val messageId: String = "",
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val seenBy: List<String> = emptyList()
)