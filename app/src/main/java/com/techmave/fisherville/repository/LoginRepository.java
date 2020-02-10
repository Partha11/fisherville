package com.techmave.fisherville.repository;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.techmave.fisherville.model.AuthCallBack;

import java.util.concurrent.TimeUnit;

public class LoginRepository {

    public LiveData<AuthCallBack> requestOtp(String number, Activity activity) {

        MutableLiveData<AuthCallBack> callback = new MutableLiveData<>();
        AuthCallBack auth = new AuthCallBack();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(number, 60, TimeUnit.SECONDS, activity,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                        auth.setSuccess(true);
                        auth.setMessage("");
                        auth.setVerificationToken("");
                        auth.setCodeSent(false);
                        auth.setCredential(phoneAuthCredential);

                        callback.setValue(auth);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                        auth.setSuccess(false);
                        auth.setCodeSent(false);
                        auth.setCredential(null);
                        auth.setVerificationToken("");
                        auth.setMessage(e.getLocalizedMessage());

                        callback.setValue(auth);
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                        super.onCodeSent(s, forceResendingToken);

                        auth.setSuccess(true);
                        auth.setMessage("");
                        auth.setCodeSent(true);
                        auth.setVerificationToken(s);
                        auth.setCredential(null);

                        callback.setValue(auth);
                    }
                });

        return callback;
    }

    public LiveData<Boolean> authenticate(PhoneAuthCredential credential) {

        MutableLiveData<Boolean> callback = new MutableLiveData<>();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        callback.setValue(true);

                    } else {

                        callback.setValue(false);
                    }
                })
                .addOnFailureListener(task -> callback.setValue(false));

        return callback;
    }
}