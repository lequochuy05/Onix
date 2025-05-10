package com.example.shop.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.adapter.SettingAdapter
import com.example.shop.databinding.ActivitySettingBinding
import com.example.shop.model.SettingModel
import com.example.shop.repository.LoginRepository
import com.example.shop.viewModel.SettingViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import androidx.core.content.edit

class SettingActivity : BaseActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var settingAdapter: SettingAdapter

    private lateinit var settings: SettingModel

    private val settingViewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvUsername.text = getUserName(this)

        settingAdapter = SettingAdapter(
            emptyList(),
            onItemClick = { position -> settingViewModel.onSettingClicked(position) },
            onSwitchToggle = { position, isChecked -> settingViewModel.onSwitchToggle(position, isChecked) }
        )

        loadUserInfo()
        observeViewModel()
        navigationHelper()
    }

    private fun navigationHelper(){
        binding.rvSettings.apply {
            layoutManager = LinearLayoutManager(this@SettingActivity)
            adapter = settingAdapter
        }

        binding.tvEditPersonalInfo.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.backBtn.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

    }

    private fun observeViewModel() {
        settingViewModel.settings.observe(this) { settings ->
            settingAdapter.updateSettings(settings)
        }

        settingViewModel.logoutEvent.observe(this) { shouldLogout ->
            if (shouldLogout) {
                logout()
                finish()
            }
        }

//        settingViewModel.notificationState.observe(this) { isEnabled ->
//            Toast.makeText(this, "Thông báo: ${if (isEnabled) "Bật" else "Tắt"}", Toast.LENGTH_SHORT).show()
//        }

        settingViewModel.navigateToOrderTracking.observe(this) { shouldNavigate ->
            if (shouldNavigate) {
                val intent = Intent(this, OrderTrackingActivity::class.java)
                startActivity(intent)
            }
        }

        settingViewModel.navigateToHelpCenter.observe(this) { shouldNavigate ->
            if (shouldNavigate) {
                val intent = Intent(this, HelpCenterActivity::class.java)
                startActivity(intent)
            }
        }
        settingViewModel.navigateToChat.observe(this) { shouldNavigate ->
            if (shouldNavigate) {
                val intent = Intent(this, ChatBotActivity::class.java)
                startActivity(intent)
            }
        }
    }
    private fun loadUserInfo() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val database = FirebaseDatabase.getInstance().getReference("UserAccount")

        userId?.let {
            database.child(it).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val firstName = snapshot.child("firstName").getValue(String::class.java) ?: ""
                    val lastName = snapshot.child("lastName").getValue(String::class.java) ?: ""
                    val fullName = "$firstName $lastName"

                    binding.tvUsername.text = fullName

                    val encodedImage = snapshot.child("img").getValue(String::class.java)
                    if (!encodedImage.isNullOrEmpty()) {
                        val decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                        binding.ivUserAvatar.setImageBitmap(bitmap)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@SettingActivity, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }



    private fun logout() {
        val loginRepository = LoginRepository()
        loginRepository.getCurrentUser()?.let { FirebaseAuth.getInstance().signOut() }
        val sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
        sharedPreferences.edit() { clear() }
        startActivity(Intent(this, IntroActivity::class.java))
    }
}
