package com.example.shop.repository

import com.google.firebase.database.*

class SelectPaymentRepository {
    private val databaseRef = FirebaseDatabase.getInstance().getReference("UserAccount")

    fun getUserAddresses(uid: String, callback: (List<String>) -> Unit) {
        databaseRef.child(uid).child("address")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val addresses = mutableListOf<String>()
                    snapshot.children.forEach {
                        val address = it.getValue(String::class.java)
                        if (!address.isNullOrEmpty()) addresses.add(address)
                    }
                    callback(addresses)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(emptyList())
                }
            })
    }

    fun addNewAddress(uid: String, newAddress: String, callback: (Boolean) -> Unit) {
        val addressRef = databaseRef.child(uid).child("address")

        addressRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentAddresses = mutableListOf<String>()
                snapshot.children.forEach {
                    val address = it.getValue(String::class.java)
                    if (!address.isNullOrEmpty()) currentAddresses.add(address)
                }

                currentAddresses.add(newAddress)
                addressRef.setValue(currentAddresses)
                    .addOnSuccessListener { callback(true) }
                    .addOnFailureListener { callback(false) }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false)
            }
        })
    }
}