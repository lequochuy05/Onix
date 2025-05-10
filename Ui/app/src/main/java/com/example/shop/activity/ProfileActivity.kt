package com.example.shop.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shop.databinding.ActivityProfileBinding
import com.example.shop.repository.ProfileRepository
import com.example.shop.viewModel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var selectedBitmap: Bitmap? = null

    // Khởi tạo ViewModel với Repository
    private val viewModel: ProfileViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProfileViewModel(ProfileRepository()) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityProfileBinding.inflate(layoutInflater)
            setContentView(binding.root)

            viewModel.loadUserData()

            binding.backBtn.setOnClickListener { finish() }
            binding.tvChangePhoto.setOnClickListener {
                try {
                    selectImage()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Lỗi khi chọn ảnh", Toast.LENGTH_SHORT).show()
                }
            }
            binding.btnSavePhoto.setOnClickListener {
                try {
                    selectedBitmap?.let { viewModel.uploadProfileImage(it) }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Lỗi khi lưu ảnh", Toast.LENGTH_SHORT).show()
                }
            }
            binding.etBirthDate.setOnClickListener {
                try {
                    showDatePickerDialog()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Lỗi khi hiển thị lịch", Toast.LENGTH_SHORT).show()
                }
            }
            binding.btnSaveInfo.setOnClickListener {
                try {
                    saveUserInfo()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Lỗi khi lưu thông tin người dùng", Toast.LENGTH_SHORT).show()
                }
            }
            checkLoginMethod()
            binding.btnSavePassword.setOnClickListener {
                try {
                    changePassword()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Lỗi khi thay đổi mật khẩu", Toast.LENGTH_SHORT).show()
                }
            }

            observeViewModel()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Lỗi khi khởi tạo ProfileActivity", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Cập nhật màn hình hiển thị thông tin
     */
    private fun observeViewModel() {
        viewModel.userData.observe(this, Observer { user ->
            try {
                binding.etFirstName.setText(user.firstName)
                binding.etLastName.setText(user.lastName)
                binding.etPhoneNumber.setText(user.phoneNumber)
                binding.etAddress.setText(user.address?.firstOrNull() ?: "")
                binding.etBirthDate.setText(user.dob ?: "")
                user.img?.let {
                    val bitmap = viewModel.decodeBase64Image(it)
                    if(bitmap != null){
                        binding.imgProfile.setImageBitmap(bitmap)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Lỗi khi cập nhật dữ liệu người dùng", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.updateSuccess.observe(this, Observer { success ->
            try {
                if (success)
                    Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, "Lỗi cập nhật", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
    }

    /**
     * Lưu thông tin người dùng
     */
    private fun saveUserInfo() {
        try {
            val userUpdates = mapOf(
                "firstName" to binding.etFirstName.text.toString(),
                "lastName" to binding.etLastName.text.toString(),
                "phoneNumber" to binding.etPhoneNumber.text.toString(),
                "address" to listOf(binding.etAddress.text.toString()),
                "dob" to binding.etBirthDate.text.toString()
            )
            viewModel.saveUserInfo(userUpdates)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Lỗi khi lưu thông tin", Toast.LENGTH_SHORT).show()
        }
    }
    /**
     * Người dùng chọn ảnh để hiển thị
     */
    private fun selectImage() {
        try {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Lỗi khi mở bộ chọn ảnh", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Set ảnh người dùng đã chọn
     */
    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
                val imageUri = data.data
                val inputStream = contentResolver.openInputStream(imageUri!!)
                selectedBitmap = BitmapFactory.decodeStream(inputStream)
                binding.imgProfile.setImageBitmap(selectedBitmap)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Lỗi khi xử lý ảnh", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Hiển thị lịch để người dùng chọn
     */
    @SuppressLint("DefaultLocale")
    private fun showDatePickerDialog() {
        try {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, day ->
                    binding.etBirthDate.setText(String.format("%02d/%02d/%d", day, month + 1, year))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Lỗi khi hiển thị lịch", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Cập nhật mật khẩu
     */
    private fun changePassword(){
        try {
            val oldPassword = binding.etOldPassword.text.toString()
            val newPassword = binding.etNewPassword.text.toString()

            if (oldPassword.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
                return
            }

            viewModel.changePassword(oldPassword, newPassword) { success, message ->
                runOnUiThread {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    if (success) {
                        binding.etOldPassword.text.clear()
                        binding.etNewPassword.text.clear()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Lỗi khi thay đổi mật khẩu", Toast.LENGTH_SHORT).show()
        }
    }
    /**
     * Kiểm tra tài khoản đăng nhập
     */
    private fun checkLoginMethod() {
        try {
            val user = FirebaseAuth.getInstance().currentUser
            user?.let {
                var isGoogleUser = false
                var hasEmailProvider = false

                for (profile in it.providerData) {
                    if (profile.providerId == "google.com") {
                        isGoogleUser = true
                    }
                    if (profile.providerId == "password") {
                        hasEmailProvider = true
                    }
                }

                // Nếu tài khoản chỉ đăng nhập bằng Google, vô hiệu hóa đổi mật khẩu
                if (isGoogleUser && !hasEmailProvider) {
                    binding.etOldPassword.isEnabled = false
                    binding.etNewPassword.isEnabled = false
                    binding.btnSavePassword.isEnabled = false
                    Toast.makeText(this, "Bạn đang đăng nhập bằng Google, không thể đặt lại mật khẩu", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Lỗi khi kiểm tra tài khoản", Toast.LENGTH_SHORT).show()
        }
    }

}
