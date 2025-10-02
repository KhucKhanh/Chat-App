package com.example.chatapp.Data

import com.google.firebase.Timestamp

data class Message(
    val messageId: String = "",
    val senderId: String = "",
    val text: String = "",
    val timestamp: Timestamp? = null,
    val seenBy: List<String> = emptyList()
)