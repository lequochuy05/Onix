package com.example.shop.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shop.model.OrderModel
import com.example.shop.repository.OrderTrackingRepository

class OrderTrackingViewModel : ViewModel() {
    private val repository = OrderTrackingRepository()
    private val _orders = MutableLiveData<List<OrderModel>>()
    val orders: LiveData<List<OrderModel>> get() = _orders

    /**
     * Lấy đơn hàng theo userId và trạng thái (nếu có)
     */
    fun fetchOrders(userId: String, status: String? = null) {
        repository.fetchOrders(userId, status) { orderList ->
            _orders.value = orderList
        }
    }

    fun addOrder(order: OrderModel) {
        repository.addOrder(order) { success ->
            if (success) fetchOrders(order.userId)
        }
    }

    fun cancelOrder(orderId: String) {
        repository.updateOrderStatus(orderId, "Đã hủy") { success ->
            if (success) {
                _orders.value = _orders.value?.map {
                    if (it.orderId == orderId) it.copy(orderStatus = "Đã hủy") else it
                }
            }
        }
    }
}
