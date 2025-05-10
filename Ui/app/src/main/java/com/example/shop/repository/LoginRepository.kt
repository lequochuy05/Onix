package com.example.shop.repository

import com.example.shop.model.UserModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("UserAccount")

    /**
     * Login with Email / Password
     */
    fun loginWithEmail(email: String, password: String, callback: (Task<AuthResult>) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task -> callback(task) }
    }

    /**
     * Get data from Firebase realtime Database
     */
    fun getUserData(firebaseUid: String, callback: (UserModel?) -> Unit) {
        database.child(firebaseUid).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val addressList = mutableListOf<String>()
                    snapshot.child("address").children.forEach {
                        val addr = it.getValue(String::class.java)
                        if (!addr.isNullOrEmpty()) addressList.add(addr)
                    }
                    val user = UserModel(
                        id = firebaseUid,
                        email = snapshot.child("email").value?.takeIf { it is String }?.toString() ?: "",
                        firstName = snapshot.child("firstName").value?.toString() ?: "",
                        lastName = snapshot.child("lastName").value?.toString() ?: "",
                        phoneNumber = snapshot.child("phoneNumber").value?.toString() ?: "",
                        address = addressList,
                        dob = snapshot.child("dob").value?.toString() ?: "",
                        img = snapshot.child("img").value?.toString()?: "",
                        banned = snapshot.child("banned").value as? Boolean ?: false,
                    )
                    callback(user)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    /**
     * Check email exists in database
     */
    fun checkIfEmailExists(email: String, callback: (UserModel?) -> Unit) {
        database.get().addOnSuccessListener { snapshot ->
            snapshot.children.forEach { userSnapshot ->
                val emailValue = userSnapshot.child("email").value
                if (emailValue is String && emailValue == email) {  // Kiểm tra nếu là String
                    val user = userSnapshot.getValue(UserModel::class.java)
                    callback(user)
                    return@addOnSuccessListener
                }
            }
            callback(null)
        }.addOnFailureListener { callback(null) }
    }

    /**
     * Login with google
     */
    fun loginWithGoogle(googleIdToken: String, callback: (Task<AuthResult>) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(googleIdToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task -> callback(task) }
    }

    /**
     * Register new account
     */
    fun registerNewUser(user: UserModel) {
        try {
            database.child(user.id).setValue(user)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Lấy người dùng hiện tại
     */
    fun getCurrentUser() = try {
        auth.currentUser
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}