package com.movieapp.ui.dashbaordmain

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.movieapp.databinding.ActivitySplashBinding
import com.movieapp.utiltyandconstant.isConnectedToNetwork

class SplashActivity : AppCompatActivity() {
    var binding: ActivitySplashBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initUI()
    }

    @SuppressLint("SetTextI18n")
    private fun initUI() {
        binding?.txtVersionName?.text = "Ver : 1.0"
        if (isConnectedToNetwork(this)) {
            binding?.btnTryAgain?.visibility = View.GONE
            Handler().postDelayed({
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            }, 200)
        } else {
            binding?.btnTryAgain?.visibility = View.VISIBLE
            binding?.btnTryAgain?.setOnClickListener {
                initUI()
            }
        }
    }
}