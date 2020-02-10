package com.techmave.fisherville.dialog;

import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.DialogFragment;

import com.techmave.fisherville.R;
import com.techmave.fisherville.callback.OnOtpReceived;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OtpDialog extends DialogFragment {

    @BindView(R.id.otp_text)
    AppCompatEditText otpText;
    @BindView(R.id.otp_resend)
    AppCompatButton otpResend;

    private OnOtpReceived listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_otp, container, false);
        ButterKnife.bind(this, view);

        countDown();

        return view;
    }

    private void countDown() {

        String text = Objects.requireNonNull(getContext()).getResources().getString(R.string.resend);

        CountDownTimer timer = new CountDownTimer(300000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                long seconds = millisUntilFinished / 1000;
                long minutes;
                String temp = text;

                if (seconds >= 60) {

                    minutes = seconds / 60;
                    seconds = seconds - minutes * 60;

                    if (minutes > 9) {

                        if (seconds > 9) {

                            temp += " (" + minutes + ":" + seconds + ")";

                        } else {

                            temp += " (" + minutes + ":0" + seconds + ")";
                        }

                    } else {

                        if (seconds > 9) {

                            temp += " (0" + minutes + ":" + seconds + ")";

                        } else {

                            temp += " (0" + minutes + ":0" + seconds + ")";
                        }
                    }

                } else {

                    if (seconds > 9) {

                        temp += " (00:" + seconds + ")";

                    } else {

                        temp += " (00:0" + seconds + ")";
                    }
                }

                otpResend.setText(temp);
            }

            @Override
            public void onFinish() {

                otpResend.setEnabled(true);
            }
        };

        timer.start();
    }

    @Override
    public void onResume() {

        super.onResume();

        Window window = Objects.requireNonNull(getDialog()).getWindow();
        Point size = new Point();

        if (window != null) {

            Display display = window.getWindowManager().getDefaultDisplay();
            display.getSize(size);

            int width = size.x;

            window.setLayout((int) (width * 0.90), WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER_HORIZONTAL);
        }
    }

    public void setOnOtpReceived(OnOtpReceived listener) {

        this.listener = listener;
    }

    @OnClick({R.id.otp_submit, R.id.otp_resend})
    public void onViewClicked(View view) {

        switch (view.getId()) {

            case R.id.otp_submit:
                retrieveCode();
                break;

            case R.id.otp_resend:
                break;
        }
    }

    private void retrieveCode() {

        if (otpText.getText() == null || TextUtils.isEmpty(otpText.getText().toString())) {

            otpText.setError("Required");

        } else if (otpText.getText().toString().length() != 6) {

            otpText.setError("Incorrect OTP");

        } else {

            listener.onReceived(otpText.getText().toString());
            dismiss();
        }
    }
}
