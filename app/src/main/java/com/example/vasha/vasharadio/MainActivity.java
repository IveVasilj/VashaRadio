package com.example.vasha.vasharadio;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.vasha.vasharadio.gps.GPSSplashActivity;
import com.example.vasha.vasharadio.musicplayer.activity.MusicPlayerSplashActivity;
import com.example.vasha.vasharadio.weather.WeatherHomeActivity;
import com.example.vasha.vasharadio.weather.WeatherSplashActivity;
import com.example.vasha.vasharadio.youtube.YoutubeActivity;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button YTButton;
    Button MPButton;
    Button RButton;
    Button GPSButton;
    Button WButton;
    TextView date;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    private void playWelcomeSound()
    {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.welcomesound);
        mediaPlayer.start();
    }

    private void initialize()
    {
        MPButton = (Button)findViewById(R.id.musicButton);
        YTButton = (Button)findViewById(R.id.ytButton);
        RButton = (Button)findViewById(R.id.radioButton);
        GPSButton = (Button)findViewById(R.id.gpsButton);
        date = (TextView)findViewById(R.id.textView);
        WButton = (Button)findViewById(R.id.weatherBottun);

        //playWelcomeSound();
        setTextViewDate();
        setButtonListeners();
    }

    private void setButtonListeners(){
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

        GPSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GPSSplashActivity.class));
            }
        });

        WButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WeatherSplashActivity.class));
            }
        });
    }

    private void setTextViewDate(){
        String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
        date.setText(currentDateTimeString);
    }
}
