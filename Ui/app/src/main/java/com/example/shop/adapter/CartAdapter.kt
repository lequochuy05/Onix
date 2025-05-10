package com.example.shop.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.shop.helper.ManagementCart
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shop.databinding.ViewholderCartBinding
import com.example.shop.helper.ChangeNumberItemsListener
import com.example.shop.model.ItemsModel

class CartAdapter(
    val listItemSelected: ArrayList<ItemsModel>,
    context: Context,
    private val userId: String,
    var changeNumberItemsListener: ChangeNumberItemsListener? = null
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ViewholderCartBinding) : RecyclerView.ViewHolder(binding.root)

    private val managementCart = ManagementCart(context, userId)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItemSelected[position]
        holder.binding.titleTxt.text = item.title
        holder.binding.feeEachItem.text = "${item.price}  VND"
        holder.binding.totalEachItem.text = "${Math.round(item.numberInCart * item.price)} VND"
        holder.binding.numberItemTxt.text = item.numberInCart.toString()

        holder.binding.tvSize.text = if (item.selectedSize.isNotEmpty()) "Size: ${item.selectedSize}" else "Size: Not selected"

        Glide.with(holder.itemView.context)
            .load(item.picUrl[0])
            .into(holder.binding.picCart)

        holder.binding.plusCartBtn.setOnClickListener {
            managementCart.plusItem(listItemSelected, position, object : ChangeNumberItemsListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onChanged() {
                    notifyDataSetChanged()
                    changeNumberItemsListener?.onChanged()
                }
            })
        }

        holder.binding.minusCartBtn.setOnClickListener {
            managementCart.minusItem(listItemSelected, position, object : ChangeNumberItemsListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onChanged() {
                    notifyDataSetChanged()
                    changeNumberItemsListener?.onChanged()
                }
            })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: ArrayList<ItemsModel>) {
        listItemSelected.clear()
        listItemSelected.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = listItemSelected.size
}
