package com.example.shop.model

data class ChatModel(
    var message: String = "",
    var user: Boolean = true,
    var userId: String = "",
    var timestamp: Long = System.currentTimeMillis()
)
