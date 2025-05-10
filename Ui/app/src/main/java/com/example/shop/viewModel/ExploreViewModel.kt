package com.example.shop.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shop.model.ItemsModel
import com.example.shop.repository.ExploreRepository

class ExploreViewModel : ViewModel() {

    private val repository = ExploreRepository()
    private val itemsList: LiveData<List<ItemsModel>> = repository.itemsList

    private val _filteredList = MutableLiveData<List<ItemsModel>>()
    val filteredList: LiveData<List<ItemsModel>> get() = _filteredList

    init {
        itemsList.observeForever {
            _filteredList.value = it
        }
    }

    fun filterItems(searchText: String, selectedSize: String, selectedPrice: String) {
        try {
            val allItems = itemsList.value ?: return
            val filtered = allItems.filter { item ->
                val matchesSearch = item.title.lowercase().contains(searchText) ||
                        item.description.lowercase().contains(searchText)
                val matchesSize = selectedSize == "(Size) Tất cả" || item.size.contains(selectedSize)
                val matchesPrice = when (selectedPrice) {
                    "Dưới 500.000" -> item.price < 500000
                    "500.000 - 2.000.000" -> item.price in 500000.0..2000000.0
                    "Trên 2.000.000" -> item.price > 2000000
                    else -> true
                }
                matchesSearch && matchesSize && matchesPrice
            }
            _filteredList.value = filtered
        } catch (e: Exception) {
            e.printStackTrace()
            _filteredList.value = emptyList() // fallback để không crash
        }
    }

}
