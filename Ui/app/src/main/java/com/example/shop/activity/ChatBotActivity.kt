package com.example.shop.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.adapter.ChatAdapter
import com.example.shop.databinding.ActivityChatBotBinding
import com.example.shop.model.ChatModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatBotActivity : BaseActivity() {
    private lateinit var binding: ActivityChatBotBinding
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<ChatModel>()

    private lateinit var databaseRef: DatabaseReference

    private val currentUserId: String by lazy {
        FirebaseAuth.getInstance().currentUser?.uid ?: "Khách"
    }

    private val currentUserName: String by lazy {
        val prefs = getSharedPreferences("UserData", MODE_PRIVATE)
        prefs.getString("userName", "Khách") ?: "Khách"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        super.onCreate(savedInstanceState)
        binding = ActivityChatBotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupFirebase()

        binding.btnSend.setOnClickListener {
            val messageText = binding.etMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendMessage(messageText)
                binding.etMessage.text.clear()
            }
        }

        binding.backBtn.setOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(messages)
        binding.recyclerViewMessages.apply {
            layoutManager = LinearLayoutManager(this@ChatBotActivity)
            adapter = chatAdapter
        }
    }

    private fun setupFirebase() {
        databaseRef = FirebaseDatabase.getInstance().getReference("chats").child(currentUserId)

        // Gửi tin chào 1 lần nếu chưa có
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    val welcomeMsg = ChatModel(
                        message = "Xin chào, $currentUserName! Mình có thể giúp gì cho bạn?",
                        user = false,
                        userId = currentUserId,
                        timestamp = System.currentTimeMillis()
                    )
                    databaseRef.push().setValue(welcomeMsg)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        // Lắng nghe tin nhắn realtime
        databaseRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                messages.clear()
                for (data in snapshot.children) {
                    val message = data.getValue(ChatModel::class.java)
                    if (message != null) {
                        messages.add(message)
                    }
                }
                chatAdapter.notifyDataSetChanged()
                binding.recyclerViewMessages.scrollToPosition(messages.size - 1)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatBotActivity, "Lỗi tải tin nhắn", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendMessage(text: String) {
        val message = ChatModel(
            message = text,
            user = true,
            userId = currentUserId,
            timestamp = System.currentTimeMillis()
        )
        databaseRef.push().setValue(message)
    }
}
