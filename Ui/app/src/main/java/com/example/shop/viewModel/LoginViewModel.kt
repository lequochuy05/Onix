    package com.example.shop.viewModel

    import android.util.Log
    import androidx.lifecycle.LiveData
    import androidx.lifecycle.MutableLiveData
    import androidx.lifecycle.ViewModel
    import com.example.shop.model.UserModel
    import com.example.shop.repository.LoginRepository

    class LoginViewModel: ViewModel() {
        private val loginRepository = LoginRepository()

        private val _loginStatus = MutableLiveData<Boolean>()
        val loginStatus: LiveData<Boolean> get() = _loginStatus

        private val _loginError = MutableLiveData<String?>()
        val loginError: LiveData<String?> get() = _loginError

        private val _userData = MutableLiveData<UserModel?>()
        val userData: LiveData<UserModel?> get() = _userData


        /**
         * Login with email/password
         */
        fun loginUserWithEmail(email: String, password: String) {
            try {
                loginRepository.loginWithEmail(email, password) { task ->
                    if (task.isSuccessful) {
                        val user = loginRepository.getCurrentUser()
                        if (user != null && user.isEmailVerified) {
                            fetchUserData(user.uid)
                        } else {
                            _loginError.value = "Vui lòng xác thực email trước khi đăng nhập"
                            _loginStatus.value = false
                        }
                    } else {
                        _loginError.value = "Email hoặc mật khẩu không đúng"
                        _loginStatus.value = false
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _loginError.value = "Lỗi đăng nhập: ${e.message}"
            }
        }

        /**
         * check email exists while login with google
         */
        fun loginWithGoogle(googleIdToken: String) {
            try {
                loginRepository.loginWithGoogle(googleIdToken) { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = loginRepository.getCurrentUser()
                        firebaseUser?.let { user ->
                            val email = user.email ?: ""
                            loginRepository.checkIfEmailExists(email) { existingUser ->
                                if (existingUser != null) {
                                    _userData.value = existingUser
                                    _loginStatus.value = true
                                } else {
                                    val newUser = UserModel(
                                        id = user.uid,
                                        email = email,
                                        firstName = user.displayName?.split(" ")?.firstOrNull() ?: "",
                                        lastName = user.displayName?.split(" ")?.lastOrNull() ?: "",
                                        phoneNumber = "",
                                        address = listOf(),
                                        dob = "",
                                        img = "",
                                    )
                                    loginRepository.registerNewUser(newUser)
                                    _userData.value = newUser
                                    _loginStatus.value = true
                                }
                            }
                        }
                    } else {
                        _loginError.value = "Đăng nhập Google thất bại"
                        _loginStatus.value = false
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _loginError.value = "Lỗi khi đăng nhập Google: ${e.message}"
            }
        }


        /**
         * Get data user
         */
        private fun fetchUserData(firebaseUid: String) {
            try {
                loginRepository.getUserData(firebaseUid) { user ->
                    if (user != null) {
                        if (user.banned) {
                            _loginError.value = "Tài khoản của bạn đã bị cấm"
                            _loginStatus.value = false
                        } else {
                            _userData.value = user
                            _loginStatus.value = true
                        }
                    } else {
                        _loginError.value = "Không tìm thấy thông tin người dùng"
                        _loginStatus.value = false
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _loginError.value = "Lỗi lấy dữ liệu người dùng: ${e.message}"
            }
        }

    }