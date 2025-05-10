// DetailActivity.kt
package com.example.shop.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.shop.R
import com.example.shop.adapter.PicListAdapter
import com.example.shop.adapter.SizeListAdapter
import com.example.shop.databinding.ActivityDetailBinding
import com.example.shop.helper.FavoriteManager
import com.example.shop.helper.ManagementCart
import com.example.shop.model.ItemsModel

class DetailActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var item: ItemsModel
    private var numberOrder = 1
    private lateinit var managementCart: ManagementCart
    private var selectedSize: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uId = userId
        managementCart = ManagementCart(this, uId)

        getBundle()
        initList()
    }

    private fun initList() {
        Glide.with(this).load(item.picUrl[0]).into(binding.picMain)

        binding.picList.adapter = PicListAdapter(item.picUrl, binding.picMain)
        binding.picList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.sizeList.adapter = SizeListAdapter(item.size) { size ->
            selectedSize = size
        }
        binding.sizeList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.addToCartBtn.setOnClickListener {
            if (!isUserLoggedIn()) {
                showLoginDialog(this)
            } else {
                if (selectedSize.isEmpty()) {
                    Toast.makeText(this, "Please select a size", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item.numberInCart = numberOrder
                item.selectedSize = selectedSize
                managementCart.insertItems(item)
            }
        }

        binding.favBtn.setOnClickListener {
            if (!isUserLoggedIn()) {
                showLoginDialog(this)
            } else {
                if (selectedSize.isEmpty()) {
                    Toast.makeText(this, "Please select a size", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item.selectedSize = selectedSize
                if (!FavoriteManager.isFavorite(item)) {
                    FavoriteManager.addFavorite(item)
                    binding.favBtn.setImageResource(R.drawable.fav_icon)
                    binding.favBtn.setColorFilter(R.color.red)
                } else {
                    FavoriteManager.removeFavorite(item)
                    binding.favBtn.setImageResource(R.drawable.fav_icon)
                }
            }
        }

        binding.backBtn.setOnClickListener { finish() }
        binding.cartBtn.setOnClickListener {
            if (!isUserLoggedIn()) {
                showLoginDialog(this)
            } else {
                val intent = Intent(this, CartActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(intent)
            }
        }

        binding.titleTxt.text = item.title
        binding.description.text = item.description
        binding.priceTxt.text = "${item.price} VND"
        binding.ratingTxt.text = "${item.rating}"
    }

    private fun getBundle() {
        item = intent.getSerializableExtra("object") as? ItemsModel ?: ItemsModel()
    }
}
