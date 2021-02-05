package com.techmave.fisherville.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.techmave.fisherville.R
import com.techmave.fisherville.databinding.ActivitySplashBinding
import com.techmave.fisherville.util.Constants

class SplashActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)

        setContentView(binding.root)
        animateText(Constants.DIRECTION_FROM_LEFT)
    }

    private fun animateText(direction: Int) {

        val animation = AnimationUtils.loadAnimation(this, if (direction == Constants.DIRECTION_FROM_LEFT) R.anim.slide_from_left else R.anim.slide_to_right)
        animation.reset()

        binding.initializingText.clearAnimation()
        binding.initializingText.startAnimation(animation)

        if (direction == Constants.DIRECTION_FROM_RIGHT) {

            binding.initializingText.visibility = View.GONE

        } else {

            changeActivity()
        }
    }

    private fun animateImage() {

        Handler(Looper.getMainLooper()).postDelayed({

            val animation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
            animation.reset()

            binding.splashLogo.clearAnimation()
            binding.splashLogo.startAnimation(animation)
            binding.splashLogo.visibility = View.GONE

            animateText(Constants.DIRECTION_FROM_RIGHT)

        }, 1200)
    }

    private fun changeActivity() {

        animateImage()

        Handler(Looper.getMainLooper()).postDelayed({

            val user = FirebaseAuth.getInstance().currentUser

            if (user != null) {

                finish()
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in)
                startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))

            } else {

                finish()
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in)
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }
        }, 2000)
    }
}