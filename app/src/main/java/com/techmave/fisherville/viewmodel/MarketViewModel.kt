package com.techmave.fisherville.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.techmave.fisherville.model.Market
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(): ViewModel() {

    private val reference: DatabaseReference = FirebaseDatabase.getInstance().reference

    val markets = MutableLiveData(mutableListOf<Market>())

    fun getMarketItems(startId: String, limit: Int): Query {

        return if (startId.isNotEmpty()) {

            reference.child("market").orderByKey().startAt(startId).limitToFirst(limit)

        } else {

            reference.child("market").orderByKey().limitToFirst(limit)
        }
    }
}