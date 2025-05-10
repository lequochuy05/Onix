package com.example.shop.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shop.model.UserModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

import java.io.ByteArrayOutputStream

class ProfileRepository {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("UserAccount")
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
    /**
     * Lấy dữ liệu người dùng
     */
    fun getUserData(userId: String): LiveData<UserModel> {
        val userData = MutableLiveData<UserModel>()

        database.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                userData.value = user
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        return userData
    }
    /**
     * Cập nhập hình ảnh người dùng
     */
    fun uploadUserImage(userId: String, bitmap: Bitmap, callback: (Boolean) -> Unit) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val imageBytes = baos.toByteArray()
        val encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT)

        database.child(userId).child("img").setValue(encodedImage).addOnSuccessListener {
            callback(true)
        }.addOnFailureListener {
            callback(false)
        }
    }

    /**
     * Cập nhập thông tin người dùng
     */
    fun updateUserInfo(userId: String, userUpdates: Map<String, Any>, callback: (Boolean) -> Unit) {
        database.child(userId).updateChildren(userUpdates).addOnSuccessListener {
            callback(true)
        }.addOnFailureListener {
            callback(false)
        }
    }

    /**
     * Mã hóa hình ảnh hiển thị
     */
    fun decodeBase64Image(encodedString: String?): Bitmap? {
        return if (!encodedString.isNullOrEmpty()) {
            val decodedBytes = Base64.decode(encodedString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } else null
    }
    /**
     * Cập nhập mật khẩu
     */
    fun changePassword(oldPassword: String, newPassword: String, callback: (Boolean, String) -> Unit) {
        val user = auth.currentUser
        val email = user?.email

        if (email != null) {
            val credential = EmailAuthProvider.getCredential(email, oldPassword)

            // Xác thực mật khẩu cũ
            user.reauthenticate(credential)
                .addOnSuccessListener {
                    // Nếu đúng, cập nhật mật khẩu mới
                    user.updatePassword(newPassword)
                        .addOnSuccessListener {
                            callback(true, "Mật khẩu đã được cập nhật thành công.")
                        }
                        .addOnFailureListener { e ->
                            callback(false, e.localizedMessage ?: "Có lỗi xảy ra.")
                        }
                }
                .addOnFailureListener {
                    callback(false, "Mật khẩu hiện tại không đúng.")
                }
        } else {
            callback(false, "Lỗi xác thực người dùng.")
        }
    }

}
