package com.example.shop.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.example.shop.R
import com.google.firebase.auth.FirebaseAuth
import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

open class BaseActivity : AppCompatActivity() {
    val userId: String
        get() = getIdUser(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,

            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Lấy tên người dùng
     */
    fun getUserName(context: Context): String {
        return try {
            val sharedPreferences = context.getSharedPreferences("UserData", MODE_PRIVATE)
            sharedPreferences.getString("userName", "Khách") ?: "Khách"
        } catch (e: Exception) {
            e.printStackTrace()
            "Khách"
        }
    }

    /**
     * Lấy tên id dùng
     */
    private fun getIdUser(context: Context): String {
        return try {
            val sharedPreferences = context.getSharedPreferences("UserData", MODE_PRIVATE)
            sharedPreferences.getString("uId", "Khách").toString()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * Kiểm tra trạng thái đăng nhập của người dùng
     */
    fun isUserLoggedIn(): Boolean {
        return try {
            FirebaseAuth.getInstance().currentUser != null
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    /**
     * Hiển thông dialog thông báo người dùng phải sử dụng tính năng đăng nhập trước
     */
    fun showLoginDialog(context: Context) {
        try {
            MaterialAlertDialogBuilder(context)
                .setTitle("Yêu cầu đăng nhập")
                .setMessage("Bạn cần đăng nhập để sử dụng tính năng này. Vui lòng đăng nhập để tiếp tục!")
                .setIcon(R.drawable.ic_warning)
                .setBackground(ContextCompat.getDrawable(context,R.drawable.bg_dialog)) // Nền tùy chỉnh
                .setPositiveButton("Đăng nhập") { _, _ ->
                    context.startActivity(Intent(context, LoginActivity::class.java))
                }
                .setNegativeButton("Hủy", null)
                .show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Lấy thời gian hiện tại
     */
    fun getCurrentDateTime(): String {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            sdf.format(Date())
        } catch (e: Exception) {
            e.printStackTrace()
            "00/00/0000 00:00:00"
        }
    }


    /**
     * Tạo chuỗi random
     */
    fun generateRandomString(length: Int): String {
        return try {
            val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
            val random = SecureRandom()
            (1..length).map { chars[random.nextInt(chars.length)] }.joinToString("")
        } catch (e: Exception) {
            e.printStackTrace()
            "RANDOM_ERROR"
        }
    }
}
