package com.example.shop.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.adapter.FavoriteAdapter
import com.example.shop.databinding.ActivityFavoriteBinding
import com.example.shop.helper.ChangeNumberItemsListener
import com.example.shop.model.ItemsModel
import com.example.shop.viewModel.FavoriteViewModel

class FavoriteActivity : BaseActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private val viewModel: FavoriteViewModel by viewModels()
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener { finish() }

        setupRecyclerView()
        observeData()
    }

    private fun setupRecyclerView() {
        binding.favoriteRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun observeData() {
        viewModel.favorites.observe(this) { list ->
            adapter = FavoriteAdapter(list.toMutableList(), this, object : ChangeNumberItemsListener {
                override fun onChanged() {
                    viewModel.loadFavorites()
                }
            })
            binding.favoriteRecyclerView.adapter = adapter

            binding.tvEmptyCart.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            binding.scrollView3.visibility = if (list.isNotEmpty()) View.VISIBLE else View.GONE
        }
    }
}
