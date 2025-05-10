package com.example.shop.repository

import com.example.shop.helper.FavoriteManager
import com.example.shop.model.ItemsModel

class FavoriteRepository {

    fun getFavorites(): List<ItemsModel> {
        return FavoriteManager.getFavorites()
    }

    fun removeFavorite(item: ItemsModel) {
        FavoriteManager.removeFavorite(item)
    }

    fun addFavorite(item: ItemsModel) {
        FavoriteManager.addFavorite(item)
    }

    fun isFavorite(item: ItemsModel): Boolean {
        return FavoriteManager.isFavorite(item)
    }
}
