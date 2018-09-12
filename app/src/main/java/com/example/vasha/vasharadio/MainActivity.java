package com.example.vasha.vasharadio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.vasha.vasharadio.musicplayer.MusicPlayerSplashActivity;
import com.example.vasha.vasharadio.youtube.YoutubeActivity;

public class MainActivity extends AppCompatActivity {

    Button YTButton;
    Button MPButton;
    Button RButton;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MPButton = (Button)findViewById(R.id.musicButton);
        YTButton = (Button)findViewById(R.id.ytButton);
        RButton = (Button)findViewById(R.id.radioButton);

        RButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        MPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MusicPlayerSplashActivity.class));
            }
        });

        YTButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, YoutubeActivity.class));
            }
        });
    }
}
