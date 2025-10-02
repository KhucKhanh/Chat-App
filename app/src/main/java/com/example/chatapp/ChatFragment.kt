package com.example.chatapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.Adapter.MessageAdapter
import com.example.chatapp.Data.Message
import com.example.chatapp.Repository.ChatRepository
import com.example.chatapp.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ChatFragment: Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MessageAdapter
    private val messages = mutableListOf<Message>()

    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private val db = FirebaseFirestore.getInstance()
    private lateinit var roomId: String


    private val chatRef by lazy {
        db.collection("chatRooms").document(roomId).collection("messages")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        roomId = arguments?.getString(ARG_ROOM_ID)
            ?: throw IllegalArgumentException("roomId missing")
    }

    companion object {
        private const val ARG_ROOM_ID = "room_id"

        fun newInstance(roomId: String): ChatFragment {
            val fragment = ChatFragment()
            val args = Bundle()
            args.putString(ARG_ROOM_ID, roomId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MessageAdapter(messages, currentUserId)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true
        binding.rvMessages.layoutManager = layoutManager

        binding.rvMessages.adapter = adapter
        listenMessages()

        binding.layoutInput.btnSend.setOnClickListener {
            val text = binding.layoutInput.etMessage.text.toString().trim()
            if (text.isNotEmpty()) {
                ChatRepository.sendMessage(roomId, currentUserId, text) { _, _ -> }
                binding.layoutInput.etMessage.text.clear()
            }
        }
    }

    private fun listenMessages() {
        chatRef.orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    Log.d("ChatFragment", "Firestore error: $error")
                    return@addSnapshotListener
                }

                Log.d("ChatFragment", "Documents size: ${snapshot.documents.size}")

                messages.clear()
                snapshot.documents.mapNotNull { it.toObject(Message::class.java) }
                    .also { messages.addAll(it) }

                adapter.notifyDataSetChanged()

                binding.rvMessages.post {
                    if (messages.isNotEmpty()) {
                        binding.rvMessages.scrollToPosition(messages.size - 1)
                    }
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}