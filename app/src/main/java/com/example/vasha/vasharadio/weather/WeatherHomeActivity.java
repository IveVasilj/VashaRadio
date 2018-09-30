package com.example.vasha.vasharadio.weather;

import android.app.DownloadManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vasha.vasharadio.R;



public class WeatherHomeActivity extends AppCompatActivity {
    TextView cityCountry;
    TextView main;
    TextView currentTemp;
    TextView minMaxTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_home);


        Intent intent = getIntent();

        cityCountry = (TextView)findViewById(R.id.city);
        main = (TextView)findViewById(R.id.description);
        currentTemp = (TextView)findViewById(R.id.currentTemp);
        minMaxTemp = (TextView)findViewById(R.id.minMaxTemp);


        cityCountry.setText(" " + intent.getStringExtra("city") + ", " + intent.getStringExtra("country"));
        main.setText(intent.getStringExtra("main"));
        currentTemp.setText(Integer.toString(intent.getIntExtra("currentTemp",0)) + "°");
        minMaxTemp.setText(Integer.toString(intent.getIntExtra("tempMax",0)) + "°" + " / " + Integer.toString(intent.getIntExtra("tempMin",0)) + "°");
    }




}
