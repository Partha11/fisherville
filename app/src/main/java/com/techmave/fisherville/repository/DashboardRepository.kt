package com.techmave.fisherville.repository

import com.techmave.fisherville.service.ApiClient
import com.techmave.fisherville.service.ApiService

class DashboardRepository {

    private val apiInterface = ApiClient.createService(ApiService::class.java)

    companion object {

        @Volatile
        private var instance: DashboardRepository? = null

        @JvmStatic
        fun getInstance(): DashboardRepository {

            if (instance == null) {

                instance = DashboardRepository()
            }

            return instance!!
        }
    }
}