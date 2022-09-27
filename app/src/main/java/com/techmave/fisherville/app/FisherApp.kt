package com.techmave.fisherville.app

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class FisherApp : Application(), Configuration.Provider {

    @Inject
    lateinit var factory: HiltWorkerFactory

    @Inject
    lateinit var workManager: WorkManager

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(factory)
        .setMinimumLoggingLevel(android.util.Log.INFO)
        .build()

    override fun onCreate() {

        super.onCreate()

        FirebaseApp.initializeApp(this)
        Firebase.database.setPersistenceEnabled(true)
    }
}
