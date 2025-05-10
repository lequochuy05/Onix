package com.example.shop.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shop.databinding.ViewholderFavoriteBinding
import com.example.shop.helper.ChangeNumberItemsListener
import com.example.shop.helper.FavoriteManager
import com.example.shop.model.ItemsModel

class FavoriteAdapter(
    private var favoriteList: MutableList<ItemsModel>,
    private val context: Context,
    private val changeNumberItemsListener: ChangeNumberItemsListener
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    class ViewHolder(val binding: ViewholderFavoriteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderFavoriteBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = favoriteList[position]
        with(holder.binding) {
            titleTxt.text = item.title
            tvSize.text = "Size: ${item.selectedSize.ifEmpty { "Not selected" }}"
            feeEachItem.text = "${item.price} VND"

            // Load hình ảnh sản phẩm
            Glide.with(holder.itemView.context)
                .load(item.picUrl.getOrNull(0))
                .into(picCart)

            // Xóa khỏi danh sách yêu thích
            btnRemove.setOnClickListener {
                FavoriteManager.removeFavorite(item)
                favoriteList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, favoriteList.size)

            // Gọi callback để cập nhật UI nếu danh sách trống
                changeNumberItemsListener.onChanged()
            }
        }
    }

    override fun getItemCount(): Int = favoriteList.size
}
