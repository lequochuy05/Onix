package com.example.shop.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.databinding.ViewholderItemOrderBinding
import com.example.shop.model.OrderModel

class OrderAdapter(
    private val onCancelOrder: (OrderModel) -> Unit,
    private val onOrderClick: (OrderModel) -> Unit  // Thêm sự kiện click vào đơn hàng
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private var orderList: List<OrderModel> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<OrderModel>) {
        orderList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ViewholderItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int = orderList.size

    inner class OrderViewHolder(private val binding: ViewholderItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: OrderModel) {
            binding.tvOrderId.text = "Mã đơn: ${order.orderId}"
            binding.tvOrderStatus.text = "Trạng thái: ${order.orderStatus}"
            binding.tvTotalPrice.text = "Tổng tiền: ${order.totalPrice} VND"

            // Xử lý hiển thị nút
            when (order.orderStatus) {
                "Chờ xác nhận" -> {
                    binding.btnCancelOrder.visibility = View.VISIBLE
                    binding.btnBuy.visibility = View.GONE
                }
                "Đã hủy" -> {
                    binding.btnCancelOrder.visibility = View.GONE
                    binding.btnBuy.visibility = View.VISIBLE
                }
                else -> {
                    binding.btnCancelOrder.visibility = View.GONE
                    binding.btnBuy.visibility = View.GONE
                }
            }

            // Sự kiện nhấn "Hủy đơn"
            binding.btnCancelOrder.setOnClickListener {
                onCancelOrder(order)
            }

            // Sự kiện nhấn "Mua lại"
            binding.btnBuy.setOnClickListener {
                Toast.makeText(it.context, "Chưa hoàn thành", Toast.LENGTH_SHORT).show()
            }

            // Click toàn bộ đơn hàng
            binding.root.setOnClickListener {
                onOrderClick(order)
            }
        }

    }
}