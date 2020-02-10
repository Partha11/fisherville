package com.techmave.fisherville.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.techmave.fisherville.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.initializing_text)
    AppCompatTextView initializingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        animateUi();
    }

    @Override
    protected void onStart() {

        super.onStart();
        FirebaseApp.initializeApp(this);
    }

    private void animateUi() {

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_left);
        animation.reset();

        initializingText.clearAnimation();
        initializingText.startAnimation(animation);

        changeActivity();
    }

    private void changeActivity() {

        new Handler().postDelayed(() -> {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null) {

                finish();
                overridePendingTransition(R.anim.slide_right, R.anim.stay);
                startActivity(new Intent(SplashActivity.this, DashboardActivity.class));

            } else {

                finish();
                overridePendingTransition(R.anim.slide_right, R.anim.stay);
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
        }, 1500);
    }
}
