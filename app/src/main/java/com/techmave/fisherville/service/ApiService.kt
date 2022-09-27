package com.techmave.fisherville.service

import com.techmave.fisherville.model.OpenWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("onecall")
    suspend fun getWeatherData(@Query("lat") lat: Float,
                       @Query("lon") lon: Float,
                       @Query("exclude") params: String,
                       @Query("appid") token: String,
                       @Query("units") units: String = "metric"): Response<OpenWeather>
}