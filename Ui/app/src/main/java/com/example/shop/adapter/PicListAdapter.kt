package com.example.shop.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shop.databinding.ViewholderPicListBinding

class PicListAdapter(val items:MutableList<String>, var picMain: ImageView):
    RecyclerView.Adapter<PicListAdapter.ViewHolder>()
    {
        private var selectedPosition=-1
        private var lastSelectedPosition=-1
        private lateinit var context: Context

       inner class ViewHolder(val binding: ViewholderPicListBinding):
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            context=parent.context
            val binding=ViewholderPicListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
            Glide.with(context)
                .load(items[position])
                .into(holder.binding.picList)
            holder.binding.root.setOnClickListener {
                lastSelectedPosition=selectedPosition
                selectedPosition=position
                notifyItemChanged(lastSelectedPosition)
                notifyItemChanged(selectedPosition)

                Glide.with(context)
                    .load(items[position])
                    .into(picMain)
            }
        }

        override fun getItemCount(): Int = items.size


    }