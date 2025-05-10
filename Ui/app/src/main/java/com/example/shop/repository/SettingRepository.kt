package com.example.shop.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.shop.R
import com.example.shop.model.SettingModel

class SettingRepository(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)

    fun getSettings(): List<SettingModel> {
        return listOf(
            SettingModel(R.drawable.search_icon, "Theo dõi đơn hàng"),
            SettingModel(R.drawable.ic_help, "Trung tâm trợ giúp"),
            SettingModel(R.drawable.ic_live_chat, "Hỗ trợ trực tuyến"),
            SettingModel(R.drawable.ic_warning, "Báo lỗi và góp ý"),
            SettingModel(R.drawable.ic_thongbao, "Thông báo", hasSwitch = true),
            SettingModel(R.drawable.ic_world, "Ngôn ngữ"),
            SettingModel(R.drawable.ic_logout, "Đăng xuất")
        )
    }

    fun saveNotificationState(isEnabled: Boolean) {
        sharedPreferences.edit().putBoolean("notifications_enabled", isEnabled).apply()
    }

    fun getNotificationState(): Boolean {
        return sharedPreferences.getBoolean("notifications_enabled", false)
    }

    fun logout(): Boolean {
        // Xử lý đăng xuất (Firebase, xóa SharedPreferences, v.v.)
        sharedPreferences.edit().clear().apply()
        return true
    }
}
