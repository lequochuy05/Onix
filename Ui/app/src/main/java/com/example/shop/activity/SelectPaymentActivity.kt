package com.example.shop.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.shop.databinding.ActivitySelectPaymentBinding
import com.example.shop.repository.SelectPaymentRepository
import com.example.shop.viewModel.SelectPaymentViewModel
import androidx.core.view.isGone

class SelectPaymentActivity : BaseActivity() {
    private lateinit var binding: ActivitySelectPaymentBinding
    private lateinit var adapter: ArrayAdapter<String>
    private val addressList = mutableListOf<String>()

    private val viewModel: SelectPaymentViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return SelectPaymentViewModel(SelectPaymentRepository()) as T
            }
        }
    }

    private val uid: String by lazy {
        getSharedPreferences("UserData", MODE_PRIVATE).getString("uId", "") ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        observeViewModel()
        viewModel.loadAddresses(uid)

        binding.cancelBtn.setOnClickListener { finish() }
        binding.nextBtn.setOnClickListener { processPayment() }

        binding.btnAddAddress.setOnClickListener {
            if (binding.etInputNewAddress.isGone) {
                binding.layoutSpinner.visibility = View.GONE
                binding.etInputNewAddress.visibility = View.VISIBLE
                binding.btnAddAddress.text = "Lưu Địa Chỉ"
            } else {
                val newAddress = binding.etInputNewAddress.text.toString().trim()
                if (newAddress.isNotEmpty()) {
                    viewModel.addAddress(uid, newAddress)
                } else {
                    Toast.makeText(this, "Vui lòng nhập địa chỉ", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupAdapter() {
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, addressList)
        binding.spinnerAddress.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.addresses.observe(this, Observer { addresses ->
            addressList.clear()
            addressList.addAll(addresses)
            adapter.notifyDataSetChanged()
            binding.spinnerAddress.setSelection(addressList.lastIndex)
        })

        viewModel.addSuccess.observe(this, Observer { success ->
            if (success) {
                Toast.makeText(this, "Địa chỉ đã được thêm", Toast.LENGTH_SHORT).show()
                resetAddressInputUI()
            } else {
                Toast.makeText(this, "Lỗi khi thêm địa chỉ", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun resetAddressInputUI() {
        binding.etInputNewAddress.text.clear()
        binding.etInputNewAddress.visibility = View.GONE
        binding.layoutSpinner.visibility = View.VISIBLE
        binding.btnAddAddress.text = "Thêm Địa Chỉ Mới"
    }

    private fun processPayment() {
        val selectedId = binding.radioGroupPayment.checkedRadioButtonId
        if (selectedId == -1) {
            Toast.makeText(this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show()
            return
        }

        val paymentMethod = when (selectedId) {
            binding.rbZaloPay.id -> "ZaloPay"
            binding.rbVnPay.id -> "VnPay"
            binding.rbCOD.id -> "COD"
            else -> ""
        }

        val selectedAddress = binding.spinnerAddress.selectedItem?.toString() ?: ""
        val totalAmount = intent.getIntExtra("totalAmount", 0)

        val intent = Intent(this, PaymentDetailActivity::class.java).apply {
            putExtra("paymentMethod", paymentMethod)
            putExtra("selectedAddress", selectedAddress)
            putExtra("totalAmount", totalAmount)
        }
        startActivity(intent)
    }
}