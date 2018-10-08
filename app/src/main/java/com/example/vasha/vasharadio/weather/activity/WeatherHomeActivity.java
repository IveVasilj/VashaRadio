package com.example.vasha.vasharadio.weather.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vasha.vasharadio.R;
import com.example.vasha.vasharadio.weather.model.ForecastModel;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


public class WeatherHomeActivity extends AppCompatActivity {
    private TextView cityCountry, main;
    private TextView currentTemp,minMaxTemp, feelslike,tempNow;
    private LinearLayout linearLayout;
    private TextView universalTextView, darkSkyTextView;
    private TextView uvindex,humidity,windDirection;
    private ArrayList<ForecastModel> hourlyForecast, weeklyForecast;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_home);

        initialize();
        setViews();
    }

    private void setHourlyForecast(ArrayList<ForecastModel> hourlyForecast) {
        Resources resources = getResources();
        String name = getPackageName();
        for(int i = 1;i<=24;i++){
            linearLayout = (LinearLayout)findViewById(resources.getIdentifier("hour" + i,"id",name));

            for(int j = 0;j<3;j+=2){
                universalTextView = (TextView) linearLayout.getChildAt(j);

                if(j == 0)
                {
                    long timeMs = hourlyForecast.get(i-1).getEpochTimestamp();
                    if(( ((((timeMs * 1000) / (1000*60*60)) % 24)) + 2) == 24)
                    {
                        universalTextView.setText("00:00");
                    }
                    else if(( ((((timeMs * 1000) / (1000*60*60)) % 24)) + 2) == 25){
                        universalTextView.setText("01:00");
                    }
                    else{
                        universalTextView.setText(Long.toString( ((((timeMs * 1000) / (1000*60*60)) % 24)) + 2) + ":00");
                    }

                }
                else{
                    universalTextView.setText(Integer.toString((int)hourlyForecast.get(i-1).getCurrentTemp()) + "°");
                }

            }
        }
    }

    private void initialize(){
        intent = getIntent();
        hourlyForecast = intent.getParcelableArrayListExtra("hourlyForecast");
        weeklyForecast = intent.getParcelableArrayListExtra("weeklyForecast");
        cityCountry = (TextView)findViewById(R.id.city);
        main = (TextView)findViewById(R.id.description);
        currentTemp = (TextView)findViewById(R.id.currentTemp);
        minMaxTemp = (TextView)findViewById(R.id.minMaxTemp);
        feelslike = (TextView)findViewById(R.id.feelslike);
        tempNow = (TextView)findViewById(R.id.tempNow);
        humidity = (TextView)findViewById(R.id.humidity);
        uvindex = (TextView)findViewById(R.id.uvindex);
        darkSkyTextView = (TextView)findViewById(R.id.darkSkyTxt);
        windDirection = (TextView)findViewById(R.id.windDirection);
    }

    private void setViews(){
        currentTemp.setText(Integer.toString((int)intent.getDoubleExtra("temp",0)) + "°");
        cityCountry.setText(" " + intent.getStringExtra("region"));
        minMaxTemp.setText(Integer.toString((int)intent.getDoubleExtra("tempMax",0.0)) + "°" + " / " + Integer.toString((int)intent.getDoubleExtra("tempMin",0.0)) + "°");
        main.setText(intent.getStringExtra("summary"));
        feelslike.setText("Feels like: " + Integer.toString((int)intent.getDoubleExtra("apparentTemp",0.0)) + "°");
        tempNow.setText(Integer.toString((int)intent.getDoubleExtra("temp",0)) + "°");
        darkSkyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://darksky.net/poweredby/")));
            }
        });
        humidity.setText(Integer.toString((int)(intent.getDoubleExtra("humidity",0.0) * 100.0)) + " %");
        uvindex.setText("UV Index: " + Integer.toString(intent.getIntExtra("uvindex",1)));
        windDirection.setText(intent.getStringExtra("windDirection"));
        setHourlyForecast(hourlyForecast);
        setWeeklyForecast(weeklyForecast);
    }

    private void setWeeklyForecast(ArrayList<ForecastModel> weeklyForecast){
        Resources resources = getResources();
        String name = getPackageName();

        for(int i = 1;i<=7;i++){
            linearLayout = (LinearLayout)findViewById(resources.getIdentifier("day" + i,"id",name));

            for(int j = 0;j<3;j+=2){
                universalTextView = (TextView) linearLayout.getChildAt(j);

                if(j == 0)
                {
                    long timeMs = weeklyForecast.get(i-1).getEpochTimestamp();
                        universalTextView.setText(DateFormat.getDateInstance().format(new Date(timeMs * 1000)));

                }
                else{
                    universalTextView.setText(Integer.toString((int)weeklyForecast.get(i-1).getTempMax()) + "°" + " / " + Integer.toString((int)weeklyForecast.get(i-1).getTempMin()) + "°");
                }

            }
        }
    }
}
