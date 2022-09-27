package com.techmave.fisherville.service

import com.techmave.fisherville.model.OpenWeather
import retrofit2.Response

interface ApiHelper {

    suspend fun getWeatherData(lat: Float, lon: Float, params: String, token: String, units: String): Response<OpenWeather>
}