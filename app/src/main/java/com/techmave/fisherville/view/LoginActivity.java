package com.techmave.fisherville.view;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.techmave.fisherville.R;
import com.techmave.fisherville.callback.OnOtpReceived;
import com.techmave.fisherville.dialog.CustomProgressDialog;
import com.techmave.fisherville.dialog.OtpDialog;
import com.techmave.fisherville.util.Constants;
import com.techmave.fisherville.util.Utility;
import com.techmave.fisherville.viewmodel.LoginViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements OnOtpReceived {

    @BindView(R.id.radio_fisherman)
    RadioButton radioFisherman;
    @BindView(R.id.radio_seller)
    RadioButton radioSeller;
    @BindView(R.id.radio_customer)
    RadioButton radioCustomer;
    @BindView(R.id.login_radio_group)
    RadioGroup loginRadioGroup;
    @BindView(R.id.login_phone_number)
    TextInputEditText loginPhoneNumber;
    @BindView(R.id.terms_text)
    AppCompatTextView termsText;

    private LoginViewModel viewModel;
    private CustomProgressDialog dialog;

    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initialize();
    }

    private void initialize() {

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        dialog = new CustomProgressDialog();

        dialog.setCancelable(false);

        termsText.setText(Utility.fromHtml(getString(R.string.terms_message)));
        loginRadioGroup.check(R.id.radio_fisherman);
    }

    @OnClick(R.id.login_button)
    public void onViewClicked() {

        if (loginPhoneNumber.getText() == null || TextUtils.isEmpty(loginPhoneNumber.getText().toString().trim())) {

            loginPhoneNumber.setError("Required");

        } else if (loginPhoneNumber.getText().toString().length() != 11) {

            loginPhoneNumber.setError("Invalid Phone Number");

        } else {

            String number = Constants.BD_CODE + loginPhoneNumber.getText().toString();

            dialog.show(getSupportFragmentManager(), "progress");
            dialog.setMessage("Please Wait...");

            viewModel.requestOtp(number, this).observe(this, authCallback -> {

                if (authCallback != null) {

                    if (authCallback.isSuccess()) {

                        if (authCallback.getCredential() != null) {

                            authenticate(authCallback.getCredential());

                        } else if (authCallback.isCodeSent()) {

                            verificationId = authCallback.getVerificationToken();

                            new Handler().postDelayed(() -> {

                                dialog.closeDialog();
                                OtpDialog otpDialog = new OtpDialog();

                                otpDialog.setOnOtpReceived(this);
                                otpDialog.show(getSupportFragmentManager(), "otp");
                                otpDialog.setCancelable(false);

                            }, 3000);
                        }

                    } else {

                        dialog.closeDialog();
                        Toast.makeText(this, authCallback.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void authenticate(PhoneAuthCredential credential) {

        dialog.show(getSupportFragmentManager(), "progress");
        dialog.setMessage("Please Wait...");

        viewModel.authenticate(credential).observe(this, value -> {

            if (value) {

                dialog.closeDialog();

            } else {

                dialog.closeDialog();
                Toast.makeText(this, "Verification Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onReceived(String code) {

        if (code != null) {

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

            dialog.show(getSupportFragmentManager(), "progress");
            dialog.setMessage("Validating...");

            authenticate(credential);
        }
    }
}
