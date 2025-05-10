package com.example.shop.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shop.model.SettingModel
import com.example.shop.repository.SettingRepository

class SettingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SettingRepository(application)

    private val _settings = MutableLiveData<List<SettingModel>>()
    val settings: LiveData<List<SettingModel>> get() = _settings

    private val _logoutEvent = MutableLiveData<Boolean>()
    val logoutEvent: LiveData<Boolean> get() = _logoutEvent

    private val _notificationState = MutableLiveData<Boolean>()
    val notificationState: LiveData<Boolean> get() = _notificationState

    private val _navigateToOrderTracking = MutableLiveData<Boolean>()
    val navigateToOrderTracking: LiveData<Boolean> get() = _navigateToOrderTracking

    private val _navigateToHelpCenter = MutableLiveData<Boolean>()
    val navigateToHelpCenter: LiveData<Boolean> get() = _navigateToHelpCenter

    private val _navigateToChat = MutableLiveData<Boolean>()
    val navigateToChat: LiveData<Boolean> get() = _navigateToChat

    init {
        _settings.value = repository.getSettings()
        _notificationState.value = repository.getNotificationState()
    }

    fun onSettingClicked(position: Int) {
        val item = _settings.value?.get(position)
        when (item?.title) {
            "Theo dõi đơn hàng" -> _navigateToOrderTracking.value = true
            "Trung tâm trợ giúp" -> _navigateToHelpCenter.value = true
            "Hỗ trợ trực tuyến" -> _navigateToChat.value = true
            "Đăng xuất" -> {
                if (repository.logout()) {
                    _logoutEvent.value = true
                }
            }
        }
    }


    fun onSwitchToggle(position: Int, isChecked: Boolean) {
        val item = _settings.value?.get(position)
        if (item?.title == "Thông báo") {
            repository.saveNotificationState(isChecked)
            _notificationState.value = isChecked
        }
    }

}
