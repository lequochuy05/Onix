package com.example.shop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.R
import com.example.shop.databinding.ViewholderSizeBinding

class SizeListAdapter(
    private val items: MutableList<String>,
    private val onSizeSelected: (String) -> Unit // Callback trả về kích cỡ được chọn
) : RecyclerView.Adapter<SizeListAdapter.Viewholder>() {

    private var selectedPosition = -1
    private var lastSelectedPosition = -1
    private lateinit var context: Context

    class Viewholder(val binding: ViewholderSizeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        context = parent.context
        val binding = ViewholderSizeBinding.inflate(LayoutInflater.from(context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.binding.sizeTxt.text = items[position]

        holder.binding.root.setOnClickListener {
            lastSelectedPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)
            onSizeSelected(items[position])  // Gọi callback trả về kích cỡ được chọn
        }

        if (selectedPosition == position) {
            holder.binding.sizeLayout.setBackgroundResource(R.drawable.bluewhite_btn)
            holder.binding.sizeTxt.setTextColor(context.resources.getColor(R.color.white))
        } else {
            holder.binding.sizeLayout.setBackgroundResource(R.drawable.grey_bg)
            holder.binding.sizeTxt.setTextColor(context.resources.getColor(R.color.black))
        }
    }

    override fun getItemCount(): Int = items.size
}
