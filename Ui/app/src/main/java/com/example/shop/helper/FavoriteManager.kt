package com.example.shop.helper

import com.example.shop.model.ItemsModel

object FavoriteManager {
    private val favoriteList: MutableList<ItemsModel> = mutableListOf()

    fun getFavorites(): List<ItemsModel> = favoriteList

    fun isFavorite(item: ItemsModel): Boolean {
        return favoriteList.any { it.title == item.title && it.selectedSize == item.selectedSize }
    }

    fun addFavorite(item: ItemsModel): Boolean {
        return if (!isFavorite(item)) {
            favoriteList.add(item)
            true
        } else {
            false
        }
    }

    fun removeFavorite(item: ItemsModel) {
        favoriteList.removeAll { it.title == item.title && it.selectedSize == item.selectedSize }
    }

}
