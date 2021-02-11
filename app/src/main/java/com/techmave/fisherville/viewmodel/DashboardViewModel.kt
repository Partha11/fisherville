package com.techmave.fisherville.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.techmave.fisherville.model.News
import com.techmave.fisherville.model.Transaction
import com.techmave.fisherville.repository.FirebaseRepository
import java.util.*


class DashboardViewModel(application: Application): AndroidViewModel(application) {

    private val reference: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var newsReference: Query

    private var profileLiveData: FirebaseRepository? = null
    private var transactionLiveData: FirebaseRepository? = null

    fun setLiveData(uid: String) {

        val calendar = Calendar.getInstance()
        val date = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        profileLiveData = FirebaseRepository(reference.child("users").child(uid).child("profile"))
        transactionLiveData = FirebaseRepository(reference.child("users").child(uid)
                .child("transactions").child(year.toString()).child(month.toString()).child(date.toString()))

        newsReference = reference.child("news").limitToLast(50)
    }

    fun getUserProfile(): LiveData<DataSnapshot?>? {

        return profileLiveData
    }

    fun getNews(): Query {

        return newsReference
    }

    fun getAllNews(): LiveData<DataSnapshot?> {

        return FirebaseRepository(newsReference)
    }

    fun getMarketItems(startId: String, limit: Int): LiveData<DataSnapshot?> {

        return if (startId.isNotEmpty()) {

            FirebaseRepository(reference.child("market")
                    .orderByKey()
                    .startAt(startId)
                    .limitToFirst(limit))

        } else {

            FirebaseRepository(reference.child("market")
                    .orderByKey()
                    .limitToFirst(limit))
        }
    }

    fun getDailyTransactions(): LiveData<DataSnapshot?>? {

        return transactionLiveData
    }

    fun addNewTransaction(uid: String, transaction: Transaction) {

        if (uid.isNotEmpty()) {

            val calendar = Calendar.getInstance()
            val date = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)

            val trxRef = reference.child("users").child(uid).child("transactions").child(year.toString()).child(month.toString()).child(date.toString())
            val trxKey = trxRef.push().key

            if (trxKey != null) {

                transaction.id = trxKey
                trxRef.child(trxKey).setValue(transaction)
            }
        }
    }

    fun updateNewsClickCount(news: News) {

        news.clickCount++
        reference.child("news").child(news.id.toString()).child("clickCount").setValue(news.clickCount)
    }
}