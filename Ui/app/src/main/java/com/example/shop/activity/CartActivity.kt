package com.example.shop.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.adapter.CartAdapter
import com.example.shop.databinding.ActivityCartBinding
import com.example.shop.helper.ChangeNumberItemsListener
import com.example.shop.helper.ManagementCart
import com.example.shop.model.ItemsModel
import kotlin.math.roundToInt

class CartActivity : BaseActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var managementCart: ManagementCart
    private lateinit var cartAdapter: CartAdapter
    private var currentCartList: ArrayList<ItemsModel> = arrayListOf()
    private val currentUserId by lazy { userId }

    private var tax: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            binding = ActivityCartBinding.inflate(layoutInflater)
            setContentView(binding.root)

            val currentUserName = getUserName(this)
            if (currentUserName.isEmpty() || currentUserName == "Khách") {
                showLoginDialog(this)
                return
            }

            managementCart = ManagementCart(this, currentUserId)
            setVariables()
            setupRecycler()
            refreshCart()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Lỗi khi khởi tạo giỏ hàng", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            refreshCart()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupRecycler() {
        try {
            binding.cartView.layoutManager = LinearLayoutManager(this)
            cartAdapter = CartAdapter(arrayListOf(), this, currentUserId, object : ChangeNumberItemsListener {
                override fun onChanged() {
                    refreshCart()
                }
            })
            binding.cartView.adapter = cartAdapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun refreshCart() {
        try {
            currentCartList = managementCart.getListCart()
            if (currentCartList.isEmpty()) {
                showEmptyCartUI()
            } else {
                cartAdapter.updateList(currentCartList)
                binding.tvEmptyCart.visibility = View.GONE
                binding.scrollView3.visibility = View.VISIBLE
                binding.cartView.visibility = View.VISIBLE
                binding.salesLayout.visibility = View.VISIBLE
                binding.priceLayout.visibility = View.VISIBLE
                binding.paymentBtn.visibility = View.VISIBLE
                calculateCart()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun calculateCart(): Int {
        return try {
            val percentTax = 0.02
            val delivery = 10000
            val itemTotal = managementCart.getTotalFee()
            tax = (itemTotal * percentTax).roundToInt().toDouble()
            val total = (itemTotal + tax + delivery).roundToInt()

            with(binding) {
                totalFeeTxt.text = "${itemTotal.roundToInt()} VND"
                taxTxt.text = "$tax VND"
                deliveryTxt.text = "$delivery VND"
                totalTxt.text = "$total VND"
            }

            total
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    private fun showEmptyCartUI() {
        try {
            binding.tvEmptyCart.visibility = View.VISIBLE
            binding.cartView.visibility = View.GONE
            binding.salesLayout.visibility = View.GONE
            binding.priceLayout.visibility = View.GONE
            binding.paymentBtn.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setVariables() {
        try {
            binding.backBtn.setOnClickListener {
                finish()
            }

            binding.paymentBtn.setOnClickListener {
                try {
                    if (!isUserLoggedIn()) {
                        showLoginDialog(this)
                    } else {
                        val totalAmount = calculateCart()
                        val intent = Intent(this, SelectPaymentActivity::class.java)
                        intent.putExtra("totalAmount", totalAmount)
                        startActivity(intent)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Lỗi khi chuyển sang thanh toán", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
