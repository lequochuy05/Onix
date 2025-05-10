package com.example.shop.activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.shop.databinding.ActivityRegisBinding
import com.example.shop.viewModel.RegisterViewModel

class RegisterActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisBinding
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            binding = ActivityRegisBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setupUI()
            observeViewModel()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Lỗi khi khởi tạo trang đăng ký", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupUI() {
        binding.btnSignup.setOnClickListener {
            try {
                val firstName = binding.txtFirstName.text.toString().trim()
                val lastName = binding.txtLastName.text.toString().trim()
                val phone = binding.txtPhoneNumber.text.toString().trim()
                val email = binding.txtEmail.text.toString().trim()
                val password = binding.txtPassword.text.toString().trim()
                val confirmPassword = binding.txtConfirmPassword.text.toString().trim()

                if (checkAllFields(firstName, lastName, phone, email, password, confirmPassword)) {
                    // Đăng ký tài khoản
                    registerViewModel.registerUser(
                        firstName,
                        lastName,
                        phone,
                        email,
                        password,
                        listOf(),  // address
                        null,      // dob
                        null       // img
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Lỗi khi xử lý dữ liệu đăng ký", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvLogin.setOnClickListener {
            try {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun observeViewModel() {
        registerViewModel.registerStatus.observe(this, Observer { success ->
            try {
                if (success) {
                    Toast.makeText(this, "Đăng kí thành công", Toast.LENGTH_LONG).show()
                    finish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

        registerViewModel.errorMessage.observe(this, Observer { message ->
            try {
                if (message.isNotEmpty()) {
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
    }

    private fun checkAllFields(
        firstName: String,
        lastName: String,
        phone: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        try {
            if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Tất cả các trường đều phải được điền", Toast.LENGTH_SHORT).show()
                return false
            }

            if (!phone.matches(Regex("^\\d{10,11}$"))) {
                binding.txtPhoneNumber.error = "Số điện thoại không hợp lệ"
                return false
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.txtEmail.error = "Định dạng email không hợp lệ"
                return false
            }

            if (password.length < 6) {
                binding.txtPassword.error = "Mật khẩu phải có ít nhất 6 ký tự"
                return false
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show()
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Lỗi khi kiểm tra dữ liệu đầu vào", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}
