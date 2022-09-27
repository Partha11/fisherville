package com.techmave.fisherville.listener

import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Query

interface _FragmentListener {

    fun getGreetingsMessage(): String

    fun getNews(): Query
    fun getAllNews(): LiveData<DataSnapshot?>
    fun getMarketItems(id: String, limit: Int): LiveData<DataSnapshot?>
    fun getDailyTransactions(): LiveData<DataSnapshot?>?

    fun onTransactionAddClicked()
    fun onWeatherButtonClicked()
    fun onSignOutClicked()
}