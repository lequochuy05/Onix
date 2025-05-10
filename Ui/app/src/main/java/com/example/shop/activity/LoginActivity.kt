package com.example.shop.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.shop.databinding.ActivityLoginBinding
import com.example.shop.viewModel.LoginViewModel
import com.google.android.gms.auth.api.signin.*
import com.example.shop.R

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private val loginViewModel: LoginViewModel by viewModels()
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            setupUI()
            setupGoogleSignIn()
            googleSignInClient.signOut()
            observeViewModel()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Lỗi khởi tạo: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupUI() {

        binding.txtPassword.setOnTouchListener { v, event ->
            val drawableEnd = 2 // Index của drawableEnd (0: start, 1: top, 2: end, 3: bottom)

            if (event.action == android.view.MotionEvent.ACTION_UP) {
                val editText = v as EditText
                editText.compoundDrawables[drawableEnd]?.let { drawable ->
                    if (event.rawX >= (editText.right - drawable.bounds.width() - editText.paddingEnd)) {
                        isPasswordVisible = !isPasswordVisible

                        // Đổi kiểu hiển thị mật khẩu
                        if (isPasswordVisible) {
                            // Hiện mật khẩu
                            editText.transformationMethod = null
                            editText.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.ic_passwrod, 0, R.drawable.ic_visibility, 0
                            )
                        } else {
                            // Ẩn mật khẩu
                            editText.transformationMethod = android.text.method.PasswordTransformationMethod.getInstance()
                            editText.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.ic_passwrod, 0, R.drawable.ic_visibility_off, 0
                            )
                        }

                        // Đặt lại con trỏ ở cuối văn bản
                        editText.setSelection(editText.text.length)
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }


        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        binding.txtSignup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            try {
                val email = binding.txtEmail.text.toString().trim()
                val password = binding.txtPassword.text.toString().trim()

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập email hoặc mật khẩu", Toast.LENGTH_SHORT).show()
                } else {
                    loginViewModel.loginUserWithEmail(email, password)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Lỗi đăng nhập: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // Tự động cuộn khi EditText được focus
        binding.txtEmail.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) scrollToView(binding.txtEmail)
        }


        binding.txtPassword.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) scrollToView(binding.txtPassword)
        }

        binding.googleBtn.setOnClickListener {
            try {
                val signInIntent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Lỗi Google Sign-In", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupGoogleSignIn() {
        try {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(this, gso)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        try {
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount = task.result
                    loginViewModel.loginWithGoogle(account.idToken!!)
                } else {
                    Toast.makeText(this, "Đăng nhập Google thất bại", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Lỗi xử lý Google Sign-In", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        loginViewModel.loginStatus.observe(this) { success ->
            if (success) {
                loginViewModel.userData.value?.let { user ->
                    saveUserData(user.id, user.email, "${user.firstName} ${user.lastName}")
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                }
            }
        }

        loginViewModel.loginError.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserData(id: String, email: String, name: String) {
        try {
            val sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
            sharedPreferences.edit().apply {
                putString("uId", id)
                putString("uEmail", email)
                putString("userName", name)
                apply()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Không thể lưu thông tin người dùng", Toast.LENGTH_SHORT).show()
        }
    }

    private fun scrollToView(view: View) {
        binding.loginScrollView.post {
            binding.loginScrollView.smoothScrollTo(0, view.top)
        }
    }
}
