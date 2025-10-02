package com.example.chatapp.Data

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val avatarUrl: String = "",
    val lastSeen: Long = System.currentTimeMillis()
)