package com.example.chatapp.Repository

import com.example.chatapp.Data.ChatRoom
import com.example.chatapp.Data.Message
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

object ChatRepository {
    private val db = FirebaseFirestore.getInstance()

    fun createChatRoom(
        isGroup: Boolean,
        name: String? = null,
        avatarUrl: String? = null,
        adminId: String? = null,
        members: List<String>,
        onResult: (Boolean, String?) -> Unit
    ) {
        val roomId = UUID.randomUUID().toString()

        val chatRoom = ChatRoom(
            roomId = roomId,
            isGroup = isGroup,
            name = name,
            avatarUrl = avatarUrl,
            adminId = adminId,
            members = members,
            lastMessage = null,
        )

        db.collection("chatRooms")
            .document(roomId)
            .set(chatRoom)
            .addOnCompleteListener {
                onResult(true, roomId)
            }
            .addOnFailureListener { e ->
                onResult(false, e.message)
            }
    }

    fun sendMessage(
        roomId: String,
        senderId: String,
        text: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val messageId = UUID.randomUUID().toString()

        val message = Message(
            messageId = messageId,
            senderId = senderId,
            text = text,
            timestamp = com.google.firebase.Timestamp.now()
        )

        val roomRef = db.collection("chatRooms").document(roomId)

        roomRef.collection("messages")
            .document(messageId)
            .set(message)
            .addOnCompleteListener {
                roomRef.update("lastMessage", message)
                onResult(true, messageId)
            }
            .addOnFailureListener { e ->
                onResult(false, e.message)
            }
    }
}