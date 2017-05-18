package me.chandansharma.aroundme.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import me.chandansharma.aroundme.R;

public class SplashScreenActivity extends AppCompatActivity {

    //Splash Screen Timer
    private static final int SPLASH_SCREEN_TIMER = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {

            /**
             * Showing Splash Screen With timer
             * and it is used for to display company logo
             */

            @Override
            public void run() {
                //Start HomeScreenActivity
                startActivity(new Intent(SplashScreenActivity.this, HomeScreenActivity.class));

                //Stop SplashScreenActivity
                finish();
            }
        }, SPLASH_SCREEN_TIMER);
    }
}
