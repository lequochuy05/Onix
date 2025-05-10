package com.example.shop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.databinding.ViewholderMessageAdminBinding
import com.example.shop.databinding.ViewholderMessageUserBinding
import com.example.shop.model.ChatModel

class ChatAdapter(private val messages: List<ChatModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_ADMIN = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].user) VIEW_TYPE_USER else VIEW_TYPE_ADMIN
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_USER) {
            val binding = ViewholderMessageUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            UserMessageViewHolder(binding)
        } else {
            val binding = ViewholderMessageAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            AdminMessageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is UserMessageViewHolder) {
            holder.binding.tvMessageUser.text = message.message
        } else if (holder is AdminMessageViewHolder) {
            holder.binding.tvMessageAdmin.text = message.message
        }
    }

    override fun getItemCount(): Int = messages.size

    class UserMessageViewHolder(val binding: ViewholderMessageUserBinding) : RecyclerView.ViewHolder(binding.root)
    class AdminMessageViewHolder(val binding: ViewholderMessageAdminBinding) : RecyclerView.ViewHolder(binding.root)
}
