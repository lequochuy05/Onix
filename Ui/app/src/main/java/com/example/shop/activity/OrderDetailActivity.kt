package com.example.shop.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.adapter.OrderItemAdapter
import com.example.shop.databinding.ActivityOrderDetailBinding
import com.example.shop.model.OrderModel

class OrderDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityOrderDetailBinding
    private lateinit var orderItemAdapter: OrderItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            binding = ActivityOrderDetailBinding.inflate(layoutInflater)
            setContentView(binding.root)

            val order = intent.getSerializableExtra("order") as? OrderModel

            order?.let {
                displayOrderDetails(it)
            } ?: run {
                Toast.makeText(this, "Không có dữ liệu đơn hàng", Toast.LENGTH_SHORT).show()
                finish()
            }

            binding.backBtn.setOnClickListener { finish() }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Lỗi khi hiển thị chi tiết đơn hàng", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayOrderDetails(order: OrderModel) {
        try {
            binding.tvOrderId.text = "Mã đơn: ${order.orderId}"
            binding.tvOrderStatus.text = "Trạng thái: ${order.orderStatus}"
            binding.tvTotalPrice.text = "Tổng tiền: ${order.totalPrice} VND"
            binding.tvOrderDate.text = "Ngày đặt: ${order.orderDate}"
            binding.tvDeliveryAddress.text = "Địa chỉ: ${order.deliveryAddress}"

            orderItemAdapter = OrderItemAdapter()
            orderItemAdapter.submitList(order.items)
            binding.recyclerViewItems.layoutManager = LinearLayoutManager(this)
            binding.recyclerViewItems.adapter = orderItemAdapter
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Lỗi hiển thị thông tin đơn hàng", Toast.LENGTH_SHORT).show()
        }
    }
}
