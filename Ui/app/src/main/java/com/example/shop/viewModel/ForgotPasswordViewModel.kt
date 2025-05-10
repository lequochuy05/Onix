package com.example.shop.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop.repository.ForgotPasswordRepository
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val repository: ForgotPasswordRepository
) : ViewModel() {

    // Kết quả trả về sau khi gửi email (thành công/thất bại)
    private val _resetStatus = MutableLiveData<Result<String>>()
    val resetStatus: LiveData<Result<String>> = _resetStatus

    fun resetPassword(email: String) {
        viewModelScope.launch {
            val result = repository.sendResetEmail(email)
            _resetStatus.value = result
        }
    }
}
