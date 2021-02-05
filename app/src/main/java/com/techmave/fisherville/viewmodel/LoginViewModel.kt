package com.techmave.fisherville.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.techmave.fisherville.repository.LoginRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LoginRepository = LoginRepository.getInstance()

    fun authenticate(credential: PhoneAuthCredential): LiveData<Boolean> {

        val callback = MutableLiveData<Boolean>()
        val auth = FirebaseAuth.getInstance()

        auth.signInWithCredential(credential).addOnCompleteListener {

            if (it.isSuccessful) {

                callback.setValue(true)

            } else {

                callback.setValue(false)
            }

        }.addOnFailureListener {

            callback.setValue(false)
        }

        return callback
    }
}
