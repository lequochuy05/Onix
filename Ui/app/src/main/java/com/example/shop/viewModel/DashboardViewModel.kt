package com.example.shop.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.shop.model.CategoryModel
import com.example.shop.model.ItemsModel
import com.example.shop.repository.DashboardRepository


class DashboardViewModel : ViewModel() {
    private val repository = DashboardRepository()

    val category: LiveData<MutableList<CategoryModel>> = repository.loadCategory()
    val bestSeller: LiveData<MutableList<ItemsModel>> = repository.loadBestSeller()

    fun loadItems(categoryId:String): LiveData<MutableList<ItemsModel>> {
        return repository.loadItems(categoryId)
    }
    fun loadUserProfile(userId: String): LiveData<String> {
        return repository.loadUserProfile(userId)
    }
}