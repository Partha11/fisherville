package com.techmave.fisherville.app

import android.app.Application
import com.google.firebase.FirebaseApp

class FisherApp : Application() {

    override fun onCreate() {

        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
