package com.example.shop.viewModel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shop.model.UserModel
import com.example.shop.repository.ProfileRepository

class ProfileViewModel(profileRepository: ProfileRepository) : ViewModel() {
    private val repository = ProfileRepository()

    private val _userData = MutableLiveData<UserModel>()
    val userData: LiveData<UserModel> get() = _userData

    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> get() = _updateSuccess

    private val _passwordChangeStatus = MutableLiveData<Pair<Boolean, String>>()
    val passwordChangeStatus: LiveData<Pair<Boolean, String>> get() = _passwordChangeStatus

    /**
     * Lấy dữ liệu người dùng
     */
    fun loadUserData() {
        val userId = repository.getCurrentUserId()
        if (userId != null) {
            repository.getUserData(userId).observeForever {
                _userData.value = it
            }
        }
    }

    /**
     * Cập nhập hình ảnh người dùng
     */
    fun uploadProfileImage(bitmap: Bitmap) {
        val userId = repository.getCurrentUserId()
        if (userId != null) {
            repository.uploadUserImage(userId, bitmap) { success ->
                _updateSuccess.value = success
            }
        }
    }

    /**
     * Cập nhập thông tin người dùng
     */
    fun saveUserInfo(userUpdates: Map<String, Any>) {
        val userId = repository.getCurrentUserId()
        if (userId != null) {
            repository.updateUserInfo(userId, userUpdates) { success ->
                _updateSuccess.value = success
            }
        }
    }

    /**
     * Mã hóa hình ảnh hiển thị
     */
    fun decodeBase64Image(encodedString: String?): Bitmap? {
        return repository.decodeBase64Image(encodedString)
    }

    /**
     * Cập nhập mật khẩu
     */
    fun changePassword(oldPassword: String, newPassword: String, callback: (Boolean, String) -> Unit) {
        repository.changePassword(oldPassword, newPassword, callback)
    }

}
