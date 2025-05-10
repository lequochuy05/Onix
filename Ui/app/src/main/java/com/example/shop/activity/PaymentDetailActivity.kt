package com.example.shop.activity

import android.app.Dialog
import android.content.Intent

import android.graphics.Color
import android.graphics.drawable.ColorDrawable

import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.Toast
import com.example.shop.Api.CreateOrder
import com.example.shop.databinding.ActivityPaymentDetailBinding
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener
import com.example.shop.R
import com.example.shop.helper.ManagementCart
import com.example.shop.model.OrderItemModel
import com.example.shop.model.OrderModel
import com.google.firebase.database.FirebaseDatabase
import androidx.core.graphics.drawable.toDrawable

class PaymentDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityPaymentDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val paymentMethod = intent.getStringExtra("paymentMethod")
        val totalAmount = intent.getIntExtra("totalAmount", 0)

        when (paymentMethod) {
            "ZaloPay" -> {
                startZaloPayPayment(totalAmount)
            }
            "VnPay" -> {
                Toast.makeText(this, "Lỗi.. Hiện tại tính năng đang được nâng cấp", Toast.LENGTH_SHORT).show()
                finish()
            }
            "COD" -> {
                binding.paymentTitle.text = "Thanh toán khi nhận hàng"
                binding.paymentDesc.text = "Bạn sẽ thanh toán khi nhận hàng từ đơn vị vận chuyển."

            }
            else -> {
                Toast.makeText(this, "Không xác định phương thức thanh toán", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        // Nút xác nhận thanh toán
        binding.confirmPaymentBtn.setOnClickListener {
            Toast.makeText(this, "Thanh toán thành công với $paymentMethod", Toast.LENGTH_SHORT).show()
            showSuccessDialog()
        }
    }

    private fun startZaloPayPayment(amount: Int) {
        // Thiết lập StrictMode để cho phép chạy mạng trên luồng chính (chỉ dùng để debug, không nên dùng trên production)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        // Khởi tạo ZaloPay SDK
        ZaloPaySDK.init(2553, Environment.SANDBOX)
        val orderApi = CreateOrder()
        try {
            val data = orderApi.createOrder(amount.toString())

            val code = data.getString("return_code")
            if (code == "1") {
                val token = data.getString("zp_trans_token")
                ZaloPaySDK.getInstance().payOrder(this, token, "demozpdk://app", object : PayOrderListener {
                    override fun onPaymentSucceeded(transactionId: String, transToken: String, appTransID: String) {
                        Toast.makeText(this@PaymentDetailActivity, "Thanh toán thành công", Toast.LENGTH_SHORT).show()
                        showSuccessDialog()
                    }

                    override fun onPaymentCanceled(transToken: String, appTransID: String) {
                        showFailedDialog()
                    }

                    override fun onPaymentError(
                        p0: ZaloPayError?,
                        p1: String?,
                        p2: String?
                    ) {

                    }


                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
    }
    private fun showSuccessDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_success)
        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

        val btnOk = dialog.findViewById<Button>(R.id.btn_ok)
        btnOk.setOnClickListener {
            dialog.dismiss()
            saveOrderToFirebase()
        }

        dialog.setCancelable(false)
        dialog.show()
    }

    private fun goToOrderTrackingPage() {
        val intent = Intent(this, OrderTrackingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Đóng PaymentDetailActivity
    }

    private fun showFailedDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_failed)
        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable()) // Làm trong suốt nền

        val btnOk = dialog.findViewById<Button>(R.id.btn_ok)
        btnOk.setOnClickListener {
            dialog.dismiss()
            goToCartPage()
        }

        dialog.setCancelable(false)
        dialog.show()
    }

    private fun goToCartPage() {
        val intent = Intent(this, CartActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun saveOrderToFirebase() {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val ordersRef = firebaseDatabase.getReference("orders")
        val sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)

        val uId = sharedPreferences.getString("uId", "Khách").toString()
        val deliveryAddress = intent.getStringExtra("selectedAddress") ?: "Chưa có địa chỉ"
        val totalAmount = intent.getIntExtra("totalAmount", 0).toDouble()

        val cartItems = getCartItems() // Lấy danh sách sản phẩm từ giỏ hàng
        val orderId = ordersRef.push().key ?: System.currentTimeMillis().toString() // Tạo ID đơn hàng
        val orderDate = getCurrentDateTime() // Lấy thời gian hiện tại

        val order = OrderModel(
            orderId = orderId,
            userId = uId,
            items = cartItems,
            totalPrice = totalAmount,
            orderStatus = "Chờ xác nhận",
            orderDate = orderDate,
            deliveryAddress = deliveryAddress
        )

        ordersRef.child(orderId).setValue(order).addOnSuccessListener {
            clearCart() // Xóa giỏ hàng sau khi đặt hàng thành công
            goToOrderTrackingPage() // Chuyển hướng đến trang theo dõi đơn hàng
        }.addOnFailureListener {
            Toast.makeText(this, "Lỗi khi lưu đơn hàng", Toast.LENGTH_SHORT).show()
        }

    }
    private fun getCartItems(): List<OrderItemModel> {
        val sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
        val uId = sharedPreferences.getString("uId", "Khách").toString() // Lấy userId từ SharedPreferences

        val managementCart = ManagementCart(this, uId) // Truyền userId vào
        val randomString = generateRandomString(12)

        return managementCart.getListCart().map { cartItem ->
            OrderItemModel(
                itemId = randomString,
                itemName = cartItem.title,
                itemPrice = cartItem.price,
                itemQuantity = cartItem.numberInCart,
                selectedSize = cartItem.selectedSize,
                picUrl = cartItem.picUrl,  //truyền danh sách ảnh sang
            )
        }
    }

    private fun clearCart() {
        val sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
        val uId = sharedPreferences.getString("uId", "Khách").toString()
        val managementCart = ManagementCart(this, uId)
        managementCart.clearCart()
    }

}