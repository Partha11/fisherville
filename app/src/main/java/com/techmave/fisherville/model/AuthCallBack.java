package com.techmave.fisherville.model;

import com.google.firebase.auth.PhoneAuthCredential;

public class AuthCallBack {

    private boolean isSuccess;
    private boolean codeSent;
    private String message;
    private String verificationToken;
    private PhoneAuthCredential credential;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isCodeSent() {
        return codeSent;
    }

    public void setCodeSent(boolean codeSent) {
        this.codeSent = codeSent;
    }

    public PhoneAuthCredential getCredential() {
        return credential;
    }

    public void setCredential(PhoneAuthCredential credential) {
        this.credential = credential;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }
}