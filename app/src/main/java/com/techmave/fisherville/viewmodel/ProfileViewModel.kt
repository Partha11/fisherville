package com.techmave.fisherville.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.techmave.fisherville.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(): ViewModel() {

    val fishBoughtInKg = MutableLiveData<Float>()
    val fishBoughtInTk = MutableLiveData<Float>()
    val fishSoldInKg = MutableLiveData<Float>()
    val fishSoldInTk = MutableLiveData<Float>()

    private val reference = FirebaseDatabase.getInstance().reference
    private var _transactions: Query? = null

    val transactions = _transactions
    val trxList = MutableLiveData(mutableListOf<Transaction>())

    init {

        _transactions = reference.child("users").child("01758708109").child("transactions").child("2021").child("1").child("12")
    }
}