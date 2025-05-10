package com.example.shop.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.adapter.BestSellerAdapter
import com.example.shop.adapter.CategoryAdapter
import com.example.shop.databinding.ActivityDashboardBinding
import com.example.shop.repository.LoginRepository
import com.example.shop.viewModel.DashboardViewModel

class DashboardActivity : BaseActivity() {

    private val viewModel = DashboardViewModel()
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUserData()
        initCategories()
        initBestSeller()
        bottomNavigation()
    }

    private fun bottomNavigation() {

        binding.exploreBtn.setOnClickListener{ startActivity(Intent(this, ExploreActivity::class.java)) }
        binding.cartBtn.setOnClickListener {

            if(!isUserLoggedIn()){
                showLoginDialog(this)
            }else {
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
            }
        }
        binding.favBtn.setOnClickListener{
            if(!isUserLoggedIn()){
                showLoginDialog(this)
            }else {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
            }
        }
        binding.settingBtn.setOnClickListener{
            if(!isUserLoggedIn()){
                showLoginDialog(this)
            }else {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
        }

    }

    private fun initCategories() {
        binding.progressBarCategory.visibility = View.VISIBLE
        viewModel.category.observe(this, Observer {
            binding.viewCategory.layoutManager =
                LinearLayoutManager(this@DashboardActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.viewCategory.adapter = CategoryAdapter(it)
            binding.progressBarCategory.visibility = View.GONE
        })
    }

    private fun initBestSeller() {
        binding.progressBarBestSeller.visibility = View.VISIBLE
        viewModel.bestSeller.observe(this, Observer {
            binding.viewBestSeller.layoutManager =
                LinearLayoutManager(this@DashboardActivity, LinearLayoutManager.VERTICAL, false)
            binding.viewBestSeller.adapter = BestSellerAdapter(it)
            binding.progressBarBestSeller.visibility = View.GONE
        })
    }

    private fun loadUserData() {
        val loginRepository = LoginRepository()
        val firebaseUser = loginRepository.getCurrentUser()

        if (firebaseUser == null) {
            binding.tvName.text = "Khách"
        } else {
            val userId = firebaseUser.uid

            // Gọi qua ViewModel
            viewModel.loadUserProfile(userId).observe(this) { fullName ->
                binding.tvName.text = fullName
            }
        }
    }



}

