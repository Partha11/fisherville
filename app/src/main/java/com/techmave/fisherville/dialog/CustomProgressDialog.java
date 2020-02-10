package com.techmave.fisherville.dialog;

import android.graphics.Point;
import android.os.Bundle;
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
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;

import com.techmave.fisherville.R;
import com.victor.loading.rotate.RotateLoading;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomProgressDialog extends DialogFragment {

    @BindView(R.id.indeterminate_loading)
    RotateLoading progressBar;
    @BindView(R.id.indeterminate_loading_text)
    AppCompatTextView loadingText;

    private String message = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_progress, container, false);
        ButterKnife.bind(this, view);

        progressBar.start();

        if (!TextUtils.isEmpty(message)) {

            loadingText.setText(message);
        }

        return view;
    }

    public void onResume() {

        super.onResume();

        Window window = Objects.requireNonNull(getDialog()).getWindow();
        Point size = new Point();

        if (window != null) {

            Display display = window.getWindowManager().getDefaultDisplay();
            display.getSize(size);

            int width = size.x;

            window.setLayout((int) (width * 0.95), WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER_HORIZONTAL);
        }
    }

    public void setMessage(String message) {

        if (loadingText != null) {

            loadingText.setText(message);

        } else {

            this.message = message;
        }
    }

    public void closeDialog() {

        progressBar.stop();
        dismiss();
    }
}
