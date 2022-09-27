package com.techmave.fisherville.service

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.techmave.fisherville.util.Constants

class WeatherService: JobIntentService() {

    companion object {

        @JvmStatic
        fun enqueueWork(context: Context, intent: Intent) {

            enqueueWork(context, WeatherService::class.java, Constants.BACKGROUND_JOB_WEATHER, intent)
        }
    }

    override fun onHandleWork(intent: Intent) {

        if (intent.hasExtra(Constants.INTENT_FLAG_LAT) && intent.hasExtra(Constants.INTENT_FLAG_LON)) {

            val lat = intent.getFloatExtra(Constants.INTENT_FLAG_LAT, 0f)
            val lon = intent.getFloatExtra(Constants.INTENT_FLAG_LON, 0f)

            val apiInterface = ApiClient.createService(ApiService::class.java)
//            val call = apiInterface.getWeatherData(lat, lon, Constants.WEATHER_EXCLUDE_PARAMS, Constants.WEATHER_TOKEN)

//            call.enqueue(object : Callback<OpenWeather> {
//
//                override fun onResponse(call: Call<OpenWeather>, response: Response<OpenWeather>) {
//
//                    val body = response.body()
//
//                    if (body != null) {
//
//                        val prefs = SharedPrefs(applicationContext)
//                        prefs.weatherData = Gson().toJson(body)
//                    }
//                }
//
//                override fun onFailure(call: Call<OpenWeather>, t: Throwable) {
//
//                    Log.d("Weather:Service", t.message, t)
//                }
//            })
        }
    }
}