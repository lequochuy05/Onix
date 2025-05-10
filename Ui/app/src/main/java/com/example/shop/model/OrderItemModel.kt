package com.example.shop.model

import java.io.Serializable

data class OrderItemModel(
    var itemId: String = "",
    var itemName: String = "",
    var itemPrice: Double = 0.0,
    var itemQuantity: Int = 0,
    var selectedSize: String = "",
    var picUrl:List<String>? = emptyList(),
) : Serializable