package com.example.shop.activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.adapter.ListItemsAdapter
import com.example.shop.databinding.ActivityListItemsBinding
import com.example.shop.viewModel.DashboardViewModel

class ListItemsActivity : BaseActivity() {
    private lateinit var binding: ActivityListItemsBinding
    private val viewModel = DashboardViewModel()
    private var id: String = ""
    private var title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            binding = ActivityListItemsBinding.inflate(layoutInflater)
            setContentView(binding.root)

            getBundle()
            initList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initList() {
        try {
            binding.apply {
                progressBar.visibility = View.VISIBLE

                viewModel.loadItems(id).observe(this@ListItemsActivity) { items ->
                    listView.layoutManager = LinearLayoutManager(
                        this@ListItemsActivity,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    listView.adapter = ListItemsAdapter(items)
                    progressBar.visibility = View.GONE
                }

                backBtn.setOnClickListener {
                    finish()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun getBundle() {
        try {
            id = intent.getStringExtra("id") ?: ""
            title = intent.getStringExtra("title") ?: ""
            binding.categoryTxt.text = title
        } catch (e: Exception) {
            e.printStackTrace()
            id = ""
            title = ""
        }
    }
}
