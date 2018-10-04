package com.example.vasha.vasharadio.weather.activity;

import android.app.DownloadManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vasha.vasharadio.R;
import com.example.vasha.vasharadio.weather.model.ForecastModel;

import java.util.ArrayList;


public class WeatherHomeActivity extends AppCompatActivity {
    TextView cityCountry;
    TextView main;
    TextView currentTemp;
    TextView minMaxTemp;
    TextView apparentTemp;
    TextView tempNow;
    TextView hour;
    TextView tempHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_home);

        Intent intent = getIntent();

        cityCountry = (TextView)findViewById(R.id.city);
        main = (TextView)findViewById(R.id.description);
        currentTemp = (TextView)findViewById(R.id.currentTemp);
        minMaxTemp = (TextView)findViewById(R.id.minMaxTemp);
        apparentTemp = (TextView)findViewById(R.id.apparentTemp);

        currentTemp.setText(Double.toString(intent.getDoubleExtra("temp",0)) + "°");
        cityCountry.setText(" " + intent.getStringExtra("region"));
        minMaxTemp.setText(Double.toString(intent.getDoubleExtra("tempMax",0.0)) + "°" + " / " + Double.toString(intent.getDoubleExtra("tempMin",0.0)) + "°");
        main.setText(intent.getStringExtra("summary"));
        apparentTemp.setText("Feels like " + Double.toString(intent.getDoubleExtra("apparentTemp",0.0)) + "°");

        ArrayList<ForecastModel> hourlyForecast = intent.getParcelableArrayListExtra("hourlyForecast");

        tempNow.setText(Double.toString(intent.getDoubleExtra("temp",0)) + "°");

        long timeMs = hourlyForecast.get(0).getEpochTimestamp();

        hour.setText(Long.toString( ((((timeMs * 1000) / (1000*60*60)) % 24)) + 1) + ":00");

        tempHour.setText(Double.toString(hourlyForecast.get(0).getCurrentTemp()));
    }




}
