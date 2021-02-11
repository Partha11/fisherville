package com.techmave.fisherville.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.techmave.fisherville.databinding.ActivityFishBinding

class FishActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFishBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityFishBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}