package com.example.shop.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shop.model.ItemsModel
import com.example.shop.repository.FavoriteRepository

class FavoriteViewModel : ViewModel() {

    private val repository = FavoriteRepository()

    private val _favorites = MutableLiveData<List<ItemsModel>>()
    val favorites: LiveData<List<ItemsModel>> get() = _favorites

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        _favorites.value = repository.getFavorites()
    }

    fun removeFavorite(item: ItemsModel) {
        repository.removeFavorite(item)
        loadFavorites()
    }

    fun addFavorite(item: ItemsModel) {
        repository.addFavorite(item)
        loadFavorites()
    }

    fun isFavorite(item: ItemsModel): Boolean {
        return repository.isFavorite(item)
    }
}
