package com.techmave.fisherville.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.techmave.fisherville.R
import com.techmave.fisherville.model.User
import com.techmave.fisherville.model.state.DialogState
import com.techmave.fisherville.repository.LoginRepository
import com.techmave.fisherville.util.extension.ViewExtensions.isValidPhoneNumber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val repository: LoginRepository): ViewModel() {

    val tempOtp = MutableLiveData("")
    val mainOtp = MutableLiveData("")

    val numError = MutableLiveData<String>()
    val otpError = MutableLiveData<String>()

    val numValidated = MutableLiveData(false)
    val otpValidated = MutableLiveData(false)

    val user = MutableLiveData(User())
    val state = MutableLiveData<DialogState>()

    fun validateLoginForm(view: View?, number: String?) {

        if (number.isNullOrEmpty()) {

            numError.value = view?.context?.getString(R.string.label_required)

        } else if (!number.isValidPhoneNumber()) {

            numError.value = view?.context?.getString(R.string.label_invalid)

        } else {

            numValidated.value = true
        }
    }

    fun validateOtp(view: View?, code: String?) {

        CoroutineScope(viewModelScope.coroutineContext).launch {

            if (!code.isNullOrEmpty() && code.length == 6) {

                mainOtp.postValue(code).also { otpValidated.postValue(true) }

            } else {

                otpError.postValue(view?.context?.getString(R.string.label_invalid_otp))
            }
        }
    }

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
