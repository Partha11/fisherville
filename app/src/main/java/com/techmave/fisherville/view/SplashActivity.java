package com.techmave.fisherville.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.techmave.fisherville.R;
import com.techmave.fisherville.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.initializing_text)
    AppCompatTextView initializingText;
    @BindView(R.id.splash_logo)
    AppCompatImageView splashLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        animateText(Constants.DIRECTION_FROM_LEFT);
    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    private void animateText(int direction) {

        Animation animation = AnimationUtils.loadAnimation(this, (direction == Constants.DIRECTION_FROM_LEFT ?
                R.anim.slide_from_left : R.anim.slide_to_right));
        animation.reset();

        initializingText.clearAnimation();
        initializingText.startAnimation(animation);

        if (direction == Constants.DIRECTION_FROM_RIGHT) {

            initializingText.setVisibility(View.GONE);

        } else {

            changeActivity();
        }
    }

    private void animateImage() {

        new Handler().postDelayed(() -> {

            Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
            animation.reset();

            splashLogo.clearAnimation();
            splashLogo.startAnimation(animation);
            splashLogo.setVisibility(View.GONE);

            animateText(Constants.DIRECTION_FROM_RIGHT);

        }, 1200);
    }

    private void changeActivity() {

        animateImage();

        new Handler().postDelayed(() -> {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null) {

                finish();
                overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
                startActivity(new Intent(SplashActivity.this, DashboardActivity.class));

            } else {

                finish();
                overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
        }, 2000);
    }
}
