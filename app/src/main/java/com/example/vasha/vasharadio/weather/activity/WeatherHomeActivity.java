package com.example.vasha.vasharadio.weather.activity;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vasha.vasharadio.R;
import com.example.vasha.vasharadio.weather.model.ForecastModel;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class WeatherHomeActivity extends AppCompatActivity {
    TextView cityCountry;
    TextView main;
    TextView currentTemp;
    TextView minMaxTemp;
    TextView apparentTemp;
    LinearLayout linearLayout;
    TextView textView;
    ArrayList<ForecastModel> hourlyForecast;
    TextView tempNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_home);

        Intent intent = getIntent();

        hourlyForecast = intent.getParcelableArrayListExtra("hourlyForecast");



        cityCountry = (TextView)findViewById(R.id.city);
        main = (TextView)findViewById(R.id.description);
        currentTemp = (TextView)findViewById(R.id.currentTemp);
        minMaxTemp = (TextView)findViewById(R.id.minMaxTemp);
        apparentTemp = (TextView)findViewById(R.id.apparentTemp);
        tempNow = (TextView)findViewById(R.id.tempNow);

        currentTemp.setText(Integer.toString((int)intent.getDoubleExtra("temp",0)) + "°");
        cityCountry.setText(" " + intent.getStringExtra("region"));
        minMaxTemp.setText(Integer.toString((int)intent.getDoubleExtra("tempMax",0.0)) + "°" + " / " + Integer.toString((int)intent.getDoubleExtra("tempMin",0.0)) + "°");
        main.setText(intent.getStringExtra("summary"));
        apparentTemp.setText("Feels like " + Integer.toString((int)intent.getDoubleExtra("apparentTemp",0.0)) + "°");
        tempNow.setText(Integer.toString((int)intent.getDoubleExtra("temp",0)) + "°");

        setHourlyForecast(hourlyForecast);

    }

    private void setHourlyForecast(ArrayList<ForecastModel> hourlyForecast) {
        Resources resources = getResources();
        String name = getPackageName();
        for(int i = 1;i<=24;i++){
            linearLayout = (LinearLayout)findViewById(resources.getIdentifier("hour" + i,"id",name));

            for(int j = 0;j<3;j+=2){
                textView = (TextView) linearLayout.getChildAt(j);

                if(j == 0)
                {
                    long timeMs = hourlyForecast.get(i-1).getEpochTimestamp();
                    if(( ((((timeMs * 1000) / (1000*60*60)) % 24)) + 2) == 24)
                    {
                        textView.setText("00:00");
                    }
                    else if(( ((((timeMs * 1000) / (1000*60*60)) % 24)) + 2) > 24){
                        textView.setText("01:00");
                    }
                    else{
                        textView.setText(Long.toString( ((((timeMs * 1000) / (1000*60*60)) % 24)) + 2) + ":00");
                    }

                }
                else{
                    textView.setText(Integer.toString((int)hourlyForecast.get(i-1).getCurrentTemp()) + "°");
                }

            }
        }
    }

}
