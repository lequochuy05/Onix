package com.example.shop.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shop.repository.SelectPaymentRepository

class SelectPaymentViewModel(private val repository: SelectPaymentRepository) : ViewModel() {

    private val _addresses = MutableLiveData<List<String>>()
    val addresses: LiveData<List<String>> get() = _addresses

    private val _addSuccess = MutableLiveData<Boolean>()
    val addSuccess: LiveData<Boolean> get() = _addSuccess

    fun loadAddresses(uid: String) {
        repository.getUserAddresses(uid) { addressList ->
            _addresses.postValue(addressList)
        }
    }

    fun addAddress(uid: String, newAddress: String) {
        repository.addNewAddress(uid, newAddress) { success ->
            _addSuccess.postValue(success)
            if (success) loadAddresses(uid)
        }
    }
}
