package com.techmave.fisherville.service

import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val service: ApiService): ApiHelper {

    override suspend fun getWeatherData(lat: Float, lon: Float, params: String, token: String, units: String) = service.getWeatherData(lat, lon, params, token, units)
}