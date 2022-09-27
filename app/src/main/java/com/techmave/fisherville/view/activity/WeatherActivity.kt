package com.techmave.fisherville.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.techmave.fisherville.R
import com.techmave.fisherville.adapter.WeatherAdapter
import com.techmave.fisherville.databinding.ActivityWeatherBinding
import com.techmave.fisherville.model.OpenWeather
import com.techmave.fisherville.util.SharedPrefs
import com.techmave.fisherville.util.Utility

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding
    private var adapter: WeatherAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)

        setContentView(binding.root)
        initialize()
    }

    private fun initialize() {

        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = "Weather"

        val prefs = SharedPrefs(this)
        val weather: OpenWeather? = Gson().fromJson(prefs.weatherData, OpenWeather::class.java)

        adapter = WeatherAdapter(this)
        setupRecyclerView()

        if (weather != null) {

            binding.locationName.text = weather.timezone
            binding.weatherType.text = Utility.convertStringToUpperCase(weather.current?.weather?.get(0)?.description)
            binding.temperatureText.text = getString(R.string.temperature_text, Utility.convertToOneDecimalPoints(weather.current?.temp ?: 0f))

            val icon = getString(R.string.icon_api, weather.current?.weather?.get(0)?.icon)

//            Picasso.get().load(icon).into(binding.weatherThumb)

            populateRecylerView(weather)
        }
    }

    private fun setupRecyclerView() {

        binding.weatherRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.weatherRecycler.itemAnimator = DefaultItemAnimator()
        binding.weatherRecycler.adapter = adapter
    }

    private fun populateRecylerView(weather: OpenWeather) {

        adapter?.items = weather.daily
        adapter?.notifyDataSetChanged()
    }
}