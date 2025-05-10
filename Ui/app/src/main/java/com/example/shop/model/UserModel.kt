package com.example.shop.model

data class UserModel(
    val id: String ="",
    val firstName: String ="",
    val lastName: String ="",
    val phoneNumber: String ="",
    val email: String ="",
    val address: List<String>? = listOf(),
    val dob: String? = null,
    val img: String? = null,
    val banned: Boolean = false
)