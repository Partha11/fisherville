package com.techmave.fisherville.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.techmave.fisherville.model.News
import com.techmave.fisherville.model.Weather
import com.techmave.fisherville.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(): ViewModel() {

    private val reference = FirebaseDatabase.getInstance().reference
    private val _weather = FirebaseRepository(reference.child("weather"))

    val singleNews = MutableLiveData<News>()

    val news = MutableLiveData(mutableListOf<News>())
    val weather = Transformations.map(_weather) { it?.getValue(Weather::class.java) }

    fun getNews(offset: Int, limit: Int) = reference.child("news").limitToLast(limit)
}