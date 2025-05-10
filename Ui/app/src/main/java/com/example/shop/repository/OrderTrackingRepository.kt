package com.example.shop.repository

import com.example.shop.model.OrderModel
import com.google.firebase.database.*

class OrderTrackingRepository {
    private val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("orders")

    /**
     * Lấy đơn hàng theo userId (và có thể lọc theo trạng thái nếu có)
     */
    fun fetchOrders(userId: String, statusFilter: String? = null, onResult: (List<OrderModel>) -> Unit) {
        databaseRef.orderByChild("userId").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val allOrders = snapshot.children.mapNotNull { it.getValue(OrderModel::class.java) }

                    val filteredOrders = if (statusFilter != null) {
                        allOrders.filter { it.orderStatus == statusFilter }
                    } else {
                        allOrders
                    }

                    onResult(filteredOrders)
                }

                override fun onCancelled(error: DatabaseError) {
                    onResult(emptyList()) // Trả về danh sách rỗng nếu có lỗi
                }
            })
    }

    fun addOrder(order: OrderModel, onComplete: (Boolean) -> Unit) {
        val orderId = databaseRef.push().key ?: return
        order.orderId = orderId
        databaseRef.child(orderId).setValue(order).addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

    fun updateOrderStatus(orderId: String, status: String, onComplete: (Boolean) -> Unit) {
        databaseRef.child(orderId).child("orderStatus").setValue(status)
            .addOnCompleteListener { task -> onComplete(task.isSuccessful) }
    }


}
