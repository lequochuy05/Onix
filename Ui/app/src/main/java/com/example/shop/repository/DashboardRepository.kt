package com.example.shop.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shop.model.ItemsModel
import com.example.shop.model.CategoryModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class DashboardRepository {
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    /**
     * Danh mục sản phẩm
     */
    fun loadCategory(): LiveData<MutableList<CategoryModel>> {

        val categoryLiveData = MutableLiveData<MutableList<CategoryModel>>()
        val ref = firebaseDatabase.getReference("Category")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<CategoryModel>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(CategoryModel::class.java)
                    if (list != null) {
                        lists.add(list)
                    }
                }
                categoryLiveData.value = lists
                //Log.d("Firebase", "Load Category Success")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Load Category Failed: ${error.message}")
            }
        })
        return categoryLiveData
    }

    /**
     * Sản phẩm bán chạy
     */
    fun loadBestSeller(): LiveData<MutableList<ItemsModel>> {
        val bestSellerLiveData = MutableLiveData<MutableList<ItemsModel>>()
        val ref = FirebaseDatabase.getInstance().getReference("Items")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bestSellers = mutableListOf<ItemsModel>()
                for (childSnapshot in snapshot.children) {
                    val item = childSnapshot.getValue(ItemsModel::class.java)
                    if (item != null && item.bestSeller) {
                        bestSellers.add(item)
                    }
                }
                bestSellerLiveData.value = bestSellers
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Load Best Seller Failed: ${error.message}")
            }
        })

        return bestSellerLiveData
    }

    /**
     * load danh sách sản phẩm
     */
    fun loadItems(categoryId:String): LiveData<MutableList<ItemsModel>> {
        val itemsLiveData = MutableLiveData<MutableList<ItemsModel>>()
        val ref = firebaseDatabase.getReference("Items")

        val query: Query = ref.orderByChild("categoryId").equalTo(categoryId)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<ItemsModel>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(ItemsModel::class.java)
                    if (list != null) {
                        lists.add(list)
                    }
                }
                itemsLiveData.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Load Best Seller Failed: ${error.message}")
            }
        })
        return itemsLiveData
    }

    /**
     * load user
     */
    fun loadUserProfile(userId: String): LiveData<String> {
        val fullNameLiveData = MutableLiveData<String>()
        val database = firebaseDatabase.getReference("UserAccount")

        try {
            database.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val firstName = snapshot.child("firstName").getValue(String::class.java) ?: ""
                    val lastName = snapshot.child("lastName").getValue(String::class.java) ?: ""
                    val fullName = "$firstName $lastName".trim()

                    fullNameLiveData.value = if (fullName.isNotEmpty()) fullName else "Khách"
                }

                override fun onCancelled(error: DatabaseError) {
                    fullNameLiveData.value = "Khách"
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            fullNameLiveData.value = "Khách"
        }

        return fullNameLiveData
    }

}