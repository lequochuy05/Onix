package com.example.shop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.databinding.ViewholderFaqBinding
import com.example.shop.model.FaqModel

class FaqAdapter(private val items: List<FaqModel>) :
    RecyclerView.Adapter<FaqAdapter.FaqViewHolder>() {

    inner class FaqViewHolder(val binding: ViewholderFaqBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FaqModel) {
            binding.tvQuestion.text = item.question
            binding.tvAnswer.text = item.answer
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val binding = ViewholderFaqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FaqViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
