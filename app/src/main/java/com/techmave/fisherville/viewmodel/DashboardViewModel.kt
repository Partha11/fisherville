package com.techmave.fisherville.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.techmave.fisherville.repository.FirebaseRepository


class DashboardViewModel(application: Application): AndroidViewModel(application) {

    private val reference: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var profileLiveData: FirebaseRepository? = null

    fun setLiveData(uid: String) {

        profileLiveData = FirebaseRepository(reference.child("users").child(uid).child("profile"))
    }

    fun getUserProfile(): LiveData<DataSnapshot?>? {

        return profileLiveData
    }
}