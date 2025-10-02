package com.example.chatapp.Data

data class ChatRoom(
    val roomId: String = "",
    val isGroup: Boolean = false,
    val name: String? = null,
    val avatarUrl: String? = null,
    val adminId: String? = null,
    val members: List<String> = emptyList(),
    val lastMessage: Message? = null,
)