package com.example.test.ui.profile

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.test.R
import com.example.test.databinding.ActivityProfileBinding
import com.example.test.ui.ViewModelFactory
import com.example.test.ui.main.MainViewModel
import com.example.test.ui.welcome.WelcomeActivity

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var token = "token"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.logoutButton.setOnClickListener {
            viewModel.logout()
        }

        viewModel.getSession().observe(this){ user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                val mainViewModel = obtainViewModel(this@ProfileActivity)

                token = user.token
                Log.d(ContentValues.TAG, "token: $token")
                binding.emailEditTextLayout.text = user.email
                binding.nameEditTextLayout.text = user.name
//                mainViewModel.getStory(token)
//                mainViewModel.story.observe(this) { storyList ->
//                    Log.d(ContentValues.TAG, "Story: $storyList")
//                    setStoryData(storyList)
//                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[MainViewModel::class.java]
    }
}