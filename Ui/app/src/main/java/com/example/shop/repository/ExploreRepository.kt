package com.example.shop.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shop.model.ItemsModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ExploreRepository {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("Items")

    private val _itemsList = MutableLiveData<List<ItemsModel>>()
    val itemsList: LiveData<List<ItemsModel>> get() = _itemsList

    init {
        loadItemsFromFirebase()
    }

    private fun loadItemsFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = mutableListOf<ItemsModel>()
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(ItemsModel::class.java)
                    if (item != null) {
                        items.add(item)
                    }
                }
                _itemsList.postValue(items)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}