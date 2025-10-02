package com.example.chatapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.Repository.ChatRepository
import com.example.chatapp.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(saveInstanceState: Bundle?) {
        super.onCreate(saveInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if(currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        ChatRepository.createChatRoom(
            isGroup = false,
            members = listOf("uid_A", "uid_B")
        ) { success, msg ->
            if(success) {
                println("Chatroom thanh cong, id = $msg")
            } else {
                println("That bai: $msg")
            }
        }

        ChatRepository.sendMessage(
            roomId = "room_12345",
            senderId = "uid_A",
            text = "Hello bạn!"
        ) { success, msg ->
            if (success) {
                println("Tin nhắn gửi thành công: $msg")
            } else {
                println("Gửi tin nhắn lỗi: $msg")
            }
        }

    }

}
