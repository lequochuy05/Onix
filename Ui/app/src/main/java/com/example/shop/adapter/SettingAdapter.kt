package com.example.shop.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.databinding.ViewholderSettingBinding
import com.example.shop.model.SettingModel
class SettingAdapter(
    private var settingList: List<SettingModel>,
    private val onItemClick: (position: Int) -> Unit,
    private val onSwitchToggle: (position: Int, isChecked: Boolean) -> Unit
) : RecyclerView.Adapter<SettingAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ViewholderSettingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderSettingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = settingList[position]
        with(holder.binding) {
            ivIcon.setImageResource(item.icon)
            tvTitle.text = item.title

            if (item.hasSwitch) {
                switchSetting.visibility = View.VISIBLE
                ivArrow.visibility = View.GONE

                switchSetting.setOnCheckedChangeListener(null)
                switchSetting.isChecked = item.isChecked

                switchSetting.setOnCheckedChangeListener { _, isChecked ->
                    onSwitchToggle(position, isChecked)
                }

            } else {
                switchSetting.visibility = View.GONE
                ivArrow.visibility = View.VISIBLE

                root.setOnClickListener {
                    onItemClick(position)
                }
            }
        }

    }

    override fun getItemCount(): Int = settingList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateSettings(newList: List<SettingModel>) {
        settingList = newList
        notifyDataSetChanged() // Cập nhật RecyclerView
    }
}

