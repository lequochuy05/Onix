package com.example.shop.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.R
import com.example.shop.adapter.HelpCenterAdapter
import com.example.shop.databinding.ActivityHelpCenterBinding
import com.example.shop.model.HelpCenterModel
import android.content.Intent

class HelpCenterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHelpCenterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpCenterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val helpItems = listOf(
            HelpCenterModel(R.drawable.ic_order, "Vấn đề về đơn hàng"),
            HelpCenterModel(R.drawable.ic_delivery, "Giao hàng & vận chuyển"),
            HelpCenterModel(R.drawable.ic_payment, "Thanh toán & hoàn tiền"),
            HelpCenterModel(R.drawable.ic_return, "Đổi trả & bảo hành"),
            HelpCenterModel(R.drawable.ic_account1, "Tài khoản & bảo mật"),
            HelpCenterModel(R.drawable.ic_promo, "Khuyến mãi & voucher"),
            HelpCenterModel(R.drawable.ic_app_bug, "Vấn đề về ứng dụng"),
            HelpCenterModel(R.drawable.ic_contact, "Liên hệ & hỗ trợ")
        )

        val adapter = HelpCenterAdapter(helpItems) { item ->
            val intent = Intent(this, FaqActivity::class.java)
            intent.putExtra("title", item.title)
            startActivity(intent)
        }

        binding.recyclerViewHelp.apply {
            layoutManager = LinearLayoutManager(this@HelpCenterActivity)
            this.adapter = adapter
        }

        binding.backBtn.setOnClickListener { finish() }
    }
}
