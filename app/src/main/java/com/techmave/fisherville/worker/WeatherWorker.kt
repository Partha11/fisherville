package com.techmave.fisherville.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.techmave.fisherville.service.ApiHelper
import com.techmave.fisherville.util.Constants
import com.techmave.fisherville.util.SharedPrefs
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

@HiltWorker
class WeatherWorker @AssistedInject constructor(@Assisted context: Context,
                                                @Assisted params: WorkerParameters
): CoroutineWorker(context, params) {

    @Inject
    lateinit var helper: ApiHelper

    @Inject
    lateinit var prefs: SharedPrefs

    override suspend fun doWork(): Result {

        kotlin.runCatching {

            val lat = inputData.getFloat(Constants.INTENT_FLAG_LAT, 0f)
            val lon = inputData.getFloat(Constants.INTENT_FLAG_LON, 0f)

            helper.getWeatherData(lat, lon, Constants.WEATHER_EXCLUDE_PARAMS, Constants.WEATHER_TOKEN, "metric").let {

                if (it.isSuccessful) {

                    it.body()?.let { body ->

                        prefs.weatherData = Gson().toJson(body)

                        return Result.success()
                    }
                }

                throw Exception(it.message())
            }

        }.onFailure { it.printStackTrace() }

        return Result.failure()
    }
}