package com.example.shop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.databinding.ViewholderHelpCenterBinding
import com.example.shop.model.HelpCenterModel

class HelpCenterAdapter(
    private val items: List<HelpCenterModel>,
    private val onItemClick: (HelpCenterModel) -> Unit
) : RecyclerView.Adapter<HelpCenterAdapter.HelpCenterViewHolder>() {

    inner class HelpCenterViewHolder(val binding: ViewholderHelpCenterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HelpCenterModel) {
            binding.tvHelpTitle.text = item.title
            binding.imgHelpIcon.setImageResource(item.iconResId)
            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HelpCenterViewHolder {
        val binding = ViewholderHelpCenterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HelpCenterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HelpCenterViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
