package com.example.vasha.vasharadio;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.vasha.vasharadio.gps.GPSSplashActivity;
import com.example.vasha.vasharadio.musicplayer.activity.MusicPlayerSplashActivity;
import com.example.vasha.vasharadio.weather.activity.WeatherSplashActivity;
import com.example.vasha.vasharadio.youtube.YoutubeActivity;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "TestActivity";
    Button YTButton;
    Button MPButton;
    Button RButton;
    Button GPSButton;
    Button WButton;
    Button SButton;
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
        SButton = (Button)findViewById(R.id.settingsButton);

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
        SButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsHomeActivity.class));
            }
        });
    }

    private void setTextViewDate(){
        String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
        date.setText(currentDateTimeString);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "On Destroy .....");
    }
    /* (non-Javadoc)
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "On Pause .....");
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onRestart()
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "On Restart .....");
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "On Resume .....");
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onStart()
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "On Start .....");
    }
    /* (non-Javadoc)
     * @see android.app.Activity#onStop()
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "On Stop .....");
    }
}
