package com.example.shop.activity

import android.content.Intent
import android.os.Bundle
import com.example.shop.databinding.ActivityIntroBinding
import com.example.shop.repository.LoginRepository
import com.google.firebase.auth.FirebaseAuth

class IntroActivity : BaseActivity() {
    private lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bỏ đăng nhập (nếu có) và xóa dữ liệu người dùng
        binding.introBtn.setOnClickListener {
            signOutUserAndClearPrefs()
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }

        binding.signInBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun signOutUserAndClearPrefs() {
        try {
            val loginRepository = LoginRepository()
            loginRepository.getCurrentUser()?.let {
                FirebaseAuth.getInstance().signOut()
            }
            getSharedPreferences("UserData", MODE_PRIVATE).edit().clear().apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
