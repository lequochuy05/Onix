package com.example.shop.model

data class SettingModel(
    val icon: Int,
    val title: String,
    val hasSwitch: Boolean=false,
    var isChecked: Boolean = false
)
