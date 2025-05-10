package com.example.shop.model

import java.io.Serializable

data class OrderModel(
    var orderId: String = "",
    var userId: String = "",
    val items: List<OrderItemModel> = listOf(),
    var totalPrice: Double = 0.0,
    var orderStatus: String = "Chờ xác nhận",
    var orderDate: String = "",
    var deliveryAddress: String = ""
): Serializable