package com.example.shop.helper

import android.content.Context
import android.widget.Toast
import com.example.shop.model.ItemsModel

class ManagementCart(private val context: Context, private val userId: String) {

    private val tinyDB = TinyDB(context)

    // Mỗi user có key riêng cho giỏ hàng
    private fun getCartKey(): String {
        return "CartList_$userId"
    }

    /**
     * Thêm sản phẩm vào giỏ hàng
     */
    fun insertItems(item: ItemsModel) {
        val listCart = getListCart()
        val index = listCart.indexOfFirst {
            it.title == item.title && it.selectedSize == item.selectedSize
        }

        if (index != -1) {
            listCart[index].numberInCart += item.numberInCart
        } else {
            listCart.add(item)
        }

        updateCartStorage(listCart)
        Toast.makeText(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show()
    }

    /**
     * Lấy danh sách giỏ hàng theo user
     */
    fun getListCart(): ArrayList<ItemsModel> {
        return try {
            tinyDB.getListObject(getCartKey()) ?: arrayListOf()
        } catch (e: Exception) {
            e.printStackTrace()
            arrayListOf()
        }
    }

    /**
     * Cập nhật danh sách giỏ hàng vào SharedPreferences
     */
    private fun updateCartStorage(list: ArrayList<ItemsModel>) {
        tinyDB.putListObject(getCartKey(), list)
    }

    /**
     * Xoá toàn bộ giỏ hàng của user hiện tại
     */
    fun clearCart() {
        tinyDB.remove(getCartKey())
    }

    /**
     * Giảm số lượng sản phẩm
     */
    fun minusItem(listItems: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
        if (listItems[position].numberInCart <= 1) {
            listItems.removeAt(position)
        } else {
            listItems[position].numberInCart--
        }
        updateCartStorage(listItems)
        listener.onChanged()
    }

    /**
     * Tăng số lượng sản phẩm
     */
    fun plusItem(listItems: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
        listItems[position].numberInCart++
        updateCartStorage(listItems)
        listener.onChanged()
    }

    /**
     * Tính tổng tiền
     */
    fun getTotalFee(): Double {
        return getListCart().sumOf { it.price * it.numberInCart }
    }
}
