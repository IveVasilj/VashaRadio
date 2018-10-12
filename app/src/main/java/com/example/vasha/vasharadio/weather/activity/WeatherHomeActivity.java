package com.example.vasha.vasharadio.weather.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.vasha.vasharadio.R;
import com.example.vasha.vasharadio.weather.model.ForecastModel;
import com.github.pwittchen.weathericonview.WeatherIconView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class WeatherHomeActivity extends AppCompatActivity {
    private TextView cityCountry, main;
    private TextView currentTemp,minMaxTemp, feelslike,tempNow;
    private LinearLayout linearLayout;
    private TextView universalTextView, darkSkyTextView;
    private TextView uvindex,humidity,windDirection, windSpeed;
    private ArrayList<ForecastModel> hourlyForecast, weeklyForecast;
    private Intent intent;
    private RelativeLayout relativeLayout;
    private ScrollView scrollView;
    private TextView sunrise, sunset;
    private WeatherIconView universalWeatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_home);

        initialize();
        setBackgroundImage();
        setViews();
    }

    private void setBackgroundImage() {
        Calendar rightNow = Calendar.getInstance();
        int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);

            if(intent.getStringExtra("summary").equals("Rain")){
                scrollView.setBackgroundResource(R.drawable.thunder_weather_background);
            }
            else{
                if(currentHourIn24Format >= 6 && currentHourIn24Format <= 18) {
                    scrollView.setBackgroundResource(R.drawable.sunny_weather_background);
                }
                else{
                    scrollView.setBackgroundResource(R.drawable.night_weather_background);
                }
            }
    }

    private void setHourlyForecast(ArrayList<ForecastModel> hourlyForecast) {
        Resources resources = getResources();
        String name = getPackageName();
        for(int i = 1;i<=24;i++){
            linearLayout = (LinearLayout)findViewById(resources.getIdentifier("hour" + i,"id",name));

            for(int j = 0;j<3;j++){


                if(j == 0)
                {
                    universalTextView = (TextView) linearLayout.getChildAt(j);
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
                else if(j == 2){
                    universalTextView = (TextView) linearLayout.getChildAt(j);
                    universalTextView.setText(Integer.toString((int)hourlyForecast.get(i-1).getCurrentTemp()) + "°");
                }
                else{

                    universalWeatherIcon = (WeatherIconView)linearLayout.getChildAt(j);
                    universalWeatherIcon.setIconColor(Color.WHITE);
                    universalWeatherIcon.setIconSize(35);
                    String icon = hourlyForecast.get(i-1).getWeatherIcon();
                    setWeatherIcon(universalWeatherIcon,icon);
                }

            }
        }
    }

    private void setWeatherIcon(WeatherIconView universalWeatherIcon, String icon) {


            if(icon.equals("clear-day"))
            {
                universalWeatherIcon.setIconResource(getString(R.string.wi_day_sunny));

            }
            else if(icon.equals("clear-night"))
            {
                universalWeatherIcon.setIconResource(getString(R.string.wi_night_clear));

            }
            else if(icon.equals("partly-cloudy-day"))
            {
                universalWeatherIcon.setIconResource(getString(R.string.wi_day_cloudy));

            }
            else if(icon.equals("partly-cloudy-night"))
            {
                universalWeatherIcon.setIconResource(getString(R.string.wi_night_alt_cloudy));

            }
            else if(icon.equals("cloudy"))
            {
                universalWeatherIcon.setIconResource(getString(R.string.wi_cloud));

            }
            else if (icon.equals("rain"))
            {
                universalWeatherIcon.setIconResource(getString(R.string.wi_rain));

            }
            else if (icon.equals("sleet"))
            {
                universalWeatherIcon.setIconResource(getString(R.string.wi_sleet));
            }
            else if (icon.equals("snow"))
            {
                universalWeatherIcon.setIconResource(getString(R.string.wi_snow));

            }
            else if (icon.equals("wind"))
            {
                universalWeatherIcon.setIconResource(getString(R.string.wi_strong_wind));

            }
            else if (icon.equals("fog"))
            {
                universalWeatherIcon.setIconResource(getString(R.string.wi_fog));

            }
            else{
                universalWeatherIcon.setIconResource(getString(R.string.wi_na));

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
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        windSpeed = (TextView)findViewById(R.id.windSpeed);
        sunrise = (TextView)findViewById(R.id.sunrise);
        sunset = (TextView)findViewById(R.id.sunset);
        universalWeatherIcon = (WeatherIconView)findViewById(R.id.my_weather_icon);
    }

    private void setViews(){

        universalWeatherIcon.setIconColor(Color.WHITE);
        universalWeatherIcon.setIconSize(35);
        universalWeatherIcon.setIconResource(getString(R.string.wi_day_sunny));

        currentTemp.setText(Integer.toString((int)intent.getDoubleExtra("temp",0)) + "°");
        cityCountry.setText(" " + intent.getStringExtra("city") + ", " + intent.getStringExtra("country"));
        minMaxTemp.setText(Integer.toString((int)intent.getDoubleExtra("tempMax",0.0)) + "°" + " / " + Integer.toString((int)intent.getDoubleExtra("tempMin",0.0)) + "°");
        main.setText(intent.getStringExtra("summary"));
        feelslike.setText(Integer.toString((int)intent.getDoubleExtra("apparentTemp",0.0)) + "°");
        tempNow.setText(Integer.toString((int)intent.getDoubleExtra("temp",0)) + "°");
        darkSkyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://darksky.net/poweredby/")));
            }
        });
        humidity.setText(Integer.toString((int)(intent.getDoubleExtra("humidity",0.0) * 100.0)) + "%");
         uvindex.setText(Integer.toString(intent.getIntExtra("uvindex",1)));
        windDirection.setText(intent.getStringExtra("windDirection"));
        windSpeed.setText(Double.toString(intent.getDoubleExtra("windSpeed",0.0)) + " km/h");
        setHourlyForecast(hourlyForecast);
        setWeeklyForecast(weeklyForecast);
        setSunriseAndSunsetTime();
    }

    private void setWeeklyForecast(ArrayList<ForecastModel> weeklyForecast){
        Resources resources = getResources();
        String name = getPackageName();

        for(int i = 1;i<=7;i++){
            linearLayout = (LinearLayout)findViewById(resources.getIdentifier("day" + i,"id",name));

            for(int j = 0;j<3;j++){


                if(j == 0)
                {
                    universalTextView = (TextView) linearLayout.getChildAt(j);
                    long timeMs = weeklyForecast.get(i-1).getEpochTimestamp();
                        universalTextView.setText(DateFormat.getDateInstance().format(new Date(timeMs * 1000)));

                }
                else if(j == 2){
                    universalTextView = (TextView) linearLayout.getChildAt(j);
                    universalTextView.setText(Integer.toString((int)weeklyForecast.get(i-1).getTempMax()) + "°" + " / " + Integer.toString((int)weeklyForecast.get(i-1).getTempMin()) + "°");
                }
                else{
                    universalWeatherIcon = (WeatherIconView)linearLayout.getChildAt(j);
                    universalWeatherIcon.setIconColor(Color.WHITE);
                    universalWeatherIcon.setIconSize(30);
                    String icon = weeklyForecast.get(i-1).getWeatherIcon();
                    setWeatherIcon(universalWeatherIcon,icon);
                }

            }
        }
    }

    private void setSunriseAndSunsetTime(){
        String timerise = DateFormat.getTimeInstance().format(new Date(1539320741L * 1000));
        String timeset = DateFormat.getTimeInstance().format(new Date(1539361078L * 1000));
        sunrise.setText(timerise.toString());
        sunset.setText(timeset.toString());
    }
}
