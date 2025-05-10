package com.example.shop.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.adapter.OrderAdapter
import com.example.shop.databinding.ActivityOrderTrackingBinding
import com.example.shop.viewModel.OrderTrackingViewModel

class OrderTrackingActivity : BaseActivity() {

    private lateinit var binding: ActivityOrderTrackingBinding
    private val orderViewModel: OrderTrackingViewModel by viewModels()
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var uId:String
    private val statusOptions = listOf(
        "Tất cả", "Chờ xác nhận", "Đang xử lý", "Đang vận chuyển", "Đã nhận", "Đã hủy"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Lấy userId từ SharedPreferences (nếu cần)
        uId = getSharedPreferences("UserData", MODE_PRIVATE)
            .getString("uId", "") ?: ""

        setupUI()
        setupRecyclerView()
        setupSpinner()
        observeOrders()

        // Tải đơn hàng lần đầu (tất cả)
        orderViewModel.fetchOrders(uId)
    }

    private fun setupUI() {
        binding.backBtn.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, statusOptions)
        binding.spinnerStatusFilter.adapter = adapter

        binding.spinnerStatusFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedStatus = statusOptions[position]
                if (selectedStatus == "Tất cả") {
                    orderViewModel.fetchOrders(uId)
                } else {
                    orderViewModel.fetchOrders(uId, selectedStatus)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupRecyclerView() {
        orderAdapter = OrderAdapter(
            onCancelOrder = { order ->
                if (order.orderStatus != "Chờ xác nhận") {
                    Toast.makeText(this, "Chỉ có thể hủy đơn đang chờ xác nhận", Toast.LENGTH_SHORT).show()
                } else {
                    AlertDialog.Builder(this)
                        .setTitle("Xác nhận hủy đơn")
                        .setMessage("Bạn có chắc muốn hủy đơn hàng này không?")
                        .setPositiveButton("Xác nhận") { _, _ ->
                            orderViewModel.cancelOrder(order.orderId)
                            Toast.makeText(this, "Đã hủy đơn hàng thành công", Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton("Không", null)
                        .show()
                }
            }
            ,
            onOrderClick = { order ->
                val intent = Intent(this, OrderDetailActivity::class.java)
                intent.putExtra("order", order)
                startActivity(intent)
            }
        )

        binding.recyclerViewOrders.apply {
            layoutManager = LinearLayoutManager(this@OrderTrackingActivity)
            adapter = orderAdapter
        }
    }

    private fun observeOrders() {
        orderViewModel.orders.observe(this) { orderList ->
            if (orderList.isNullOrEmpty()) {
                binding.recyclerViewOrders.visibility = View.GONE
                binding.tvEmptyCart.visibility = View.VISIBLE
            } else {
                binding.recyclerViewOrders.visibility = View.VISIBLE
                binding.tvEmptyCart.visibility = View.GONE
                orderAdapter.submitList(orderList.reversed())
            }
        }
    }


}
