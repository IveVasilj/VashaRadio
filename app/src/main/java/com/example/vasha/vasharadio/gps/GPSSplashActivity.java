package com.example.vasha.vasharadio.gps;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.vasha.vasharadio.R;


public class GPSSplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpssplash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(GPSSplashActivity.this, GPSHomeActivity.class));
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
