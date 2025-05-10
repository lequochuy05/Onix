package com.example.shop.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shop.activity.DetailActivity
import com.example.shop.databinding.ViewholderBestSellerBinding
import com.example.shop.databinding.ViewholderItem1Binding
import com.example.shop.databinding.ViewholderItem2Binding
import com.example.shop.model.ItemsModel

class ListItemsAdapter(val items: MutableList<ItemsModel>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        companion object{
            const val TYPE_ITEM1 = 0
            const val TYPE_ITEM2 = 1
        }
private var context:Context? = null
    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) TYPE_ITEM1 else TYPE_ITEM2
    }
     class ViewholderItem1(val binding: ViewholderItem1Binding) :
        RecyclerView.ViewHolder(binding.root)

     class ViewholderItem2(val binding: ViewholderItem2Binding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        context = parent.context
        return when(viewType){
            TYPE_ITEM1 -> {
                val binding =
                    ViewholderItem1Binding.inflate(LayoutInflater.from(parent.context), parent, false)

                ViewholderItem1(binding)
            }
            TYPE_ITEM2 -> {
                val binding =
                    ViewholderItem2Binding.inflate(LayoutInflater.from(parent.context), parent, false)

                ViewholderItem2(binding)
            }else->throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        fun bindCommonData(titleTxt:String,priceTxt:String,rating:Float,picUrl:String,logo:String){
            when(holder){
                is ViewholderItem1 -> {
                    holder.binding.titleTxt.text = titleTxt
                    holder.binding.priceTxt.text = priceTxt
                    holder.binding.ratingBar.rating = rating

                    Glide.with(holder.itemView.context)
                        .load(picUrl)
                        .into(holder.binding.pic)

                    Glide.with(holder.itemView.context)
                        .load(logo)
                        .into(holder.binding.logo)

                    holder.itemView.setOnClickListener {
                        val intent = Intent(holder.itemView.context, DetailActivity::class.java)
                        intent.putExtra("object", items[position])
                        holder.itemView.context.startActivity(intent)
                    }
                }
                is ViewholderItem2 -> {
                    holder.binding.titleTxt.text = titleTxt
                    holder.binding.priceTxt.text = priceTxt
                    holder.binding.ratingBar.rating = rating

                    Glide.with(holder.itemView.context)
                        .load(picUrl)
                        .into(holder.binding.pic)

                    Glide.with(holder.itemView.context)
                        .load(logo)
                        .into(holder.binding.logo)

                    holder.itemView.setOnClickListener {
                        val intent = Intent(holder.itemView.context, DetailActivity::class.java)
                        intent.putExtra("object", items[position])
                        holder.itemView.context.startActivity(intent)
                    }
                }
            }
        }
        bindCommonData(
            item.title,
            item.price.toString() + " VND",
            item.rating.toFloat(),
            item.picUrl[0],
            item.logo
        )

    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<ItemsModel>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size
}