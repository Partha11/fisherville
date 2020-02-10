package com.techmave.fisherville.viewmodel;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.PhoneAuthCredential;
import com.techmave.fisherville.model.AuthCallBack;
import com.techmave.fisherville.repository.LoginRepository;

public class LoginViewModel extends AndroidViewModel {

    private LoginRepository repository;

    public LoginViewModel(@NonNull Application application) {

        super(application);
        this.repository = new LoginRepository();
    }

    public LiveData<AuthCallBack> requestOtp(String number, Activity activity) {

        return repository.requestOtp(number, activity);
    }

    public LiveData<Boolean> authenticate(PhoneAuthCredential credential) {

        return repository.authenticate(credential);
    }
}
