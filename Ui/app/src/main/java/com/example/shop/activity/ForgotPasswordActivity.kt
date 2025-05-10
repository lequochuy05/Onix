package com.example.shop.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.shop.databinding.ActivityForgotPasswordBinding
import com.example.shop.repository.ForgotPasswordRepository
import com.example.shop.viewModel.ForgotPasswordViewModel

class ForgotPasswordActivity : BaseActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    private val viewModel: ForgotPasswordViewModel by viewModels {
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                val repo = ForgotPasswordRepository()
                return ForgotPasswordViewModel(repo) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //  Đặt lại mật khẩu
        binding.btnSendOtp.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            if (email.isEmpty()) {
                binding.etEmail.error = "Email không được để trống"
                binding.etEmail.requestFocus()
            } else {
                viewModel.resetPassword(email)
            }
        }

        // Nút hủy bỏ
        binding.cancelBtn.setOnClickListener {
            finish()
        }

        // Quan sát kết quả đặt lại mật khẩu
        viewModel.resetStatus.observe(this, Observer { result ->
            result.onSuccess {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                finish()
            }
            result.onFailure {
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
