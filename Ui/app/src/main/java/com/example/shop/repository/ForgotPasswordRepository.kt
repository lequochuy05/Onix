package com.example.shop.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class ForgotPasswordRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun sendResetEmail(email: String): Result<String> {
        return try {
            val result = auth.fetchSignInMethodsForEmail(email).await()
            val signInMethods = result.signInMethods

            return if (signInMethods.isNullOrEmpty()) {
                Result.failure(Exception("Email chưa được đăng ký"))
            } else {
                auth.sendPasswordResetEmail(email).await()
                Result.success("Đã gửi email đặt lại mật khẩu đến $email")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
