package com.example.shop.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shop.repository.RegisterRepository

class RegisterViewModel : ViewModel() {
    private val repository = RegisterRepository()

    private val _registerStatus = MutableLiveData<Boolean>()
    val registerStatus: LiveData<Boolean> = _registerStatus

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun registerUser(firstName: String, lastName: String, phone: String, email: String, password: String, address: List<String>?, dob: String?, img:String?) {
        repository.registerUser(firstName, lastName, phone, email, password, address, dob, img) { success, message ->
            _registerStatus.value = success
            _errorMessage.value = message
        }
    }
}