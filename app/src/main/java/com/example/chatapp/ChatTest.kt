package com.example.chatapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ChatTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_test) // layout container

        // Thay "abc123" bằng roomId thật trong Firebase
        val chatFragment = ChatFragment.newInstance("abc123")

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, chatFragment)
            .commit()
    }
}
