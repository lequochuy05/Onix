package com.example.shop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shop.databinding.ViewholderOrderItemBinding
import com.example.shop.model.OrderItemModel
import com.example.shop.R

class OrderItemAdapter : RecyclerView.Adapter<OrderItemAdapter.ItemViewHolder>() {

    private var itemList: List<OrderItemModel> = emptyList()

    fun submitList(newList: List<OrderItemModel>) {
        itemList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ViewholderOrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        val binding = holder.binding

        binding.tvItemName.text = item.itemName
        binding.tvItemSize.text = "Size: ${item.selectedSize}"
        binding.tvItemPrice.text = "Giá: ${item.itemPrice} VND"
        binding.tvItemQuantity.text = "Số lượng: ${item.itemQuantity}"

        val picList = item.picUrl
        val imageUrl = if (!picList.isNullOrEmpty()) picList[0] else null

        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.shoes_sample)
            .error(R.drawable.shoes_sample)
            .into(binding.ivItemImage)
    }

    override fun getItemCount(): Int = itemList.size

    inner class ItemViewHolder(val binding: ViewholderOrderItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}