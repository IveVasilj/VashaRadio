package com.example.vasha.vasharadio.weather.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.vasha.vasharadio.R;
import com.example.vasha.vasharadio.weather.model.ForecastModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WeatherSplashActivity extends AppCompatActivity{

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;

    private static final String TAG = "JSONRequest";

    private FusedLocationProviderClient mFusedLocationClient;
    private RequestQueue queue;


    //************Overridding methods****************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_splash);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(WeatherSplashActivity.this);
        queue = Volley.newRequestQueue(this);

        checkUserPermission();
    }

    //Listens to users response to the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //getLastLocation();
                    apiRequest();
                } else {
                    Toast.makeText(WeatherSplashActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(queue!=null){
            queue.cancelAll(TAG);
        }
    }


    //***************My methods*****************

    //Checks users permission required by the application
    private void checkUserPermission() {
        if (ContextCompat.checkSelfPermission(WeatherSplashActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(WeatherSplashActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions(WeatherSplashActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            } else {
                ActivityCompat.requestPermissions(WeatherSplashActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            }
        } else {
            apiRequest();
        }
    }

    //gets last known location of the device
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            //apiRequest(location.getLatitude(), location.getLongitude());
                        }
                        else{
                            System.out.println("erorr");
                        }
                    }
                });
    }

    //Connects to darksky api and collects all the required data for weather
    private void apiRequest(/*double latitude, double longitude*/) {
        //String uri = "https://api.darksky.net/forecast/0ad19f6ac2ec3942d4300b750576a445/" + latitude + "," + longitude + "?units=auto";
        String tempUri = "https://api.darksky.net/forecast/0ad19f6ac2ec3942d4300b750576a445/43.5081,16.4402?units=auto";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, tempUri, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            double mainTemp = response.getJSONObject("currently").getDouble("temperature");
                            double apparentTemp = response.getJSONObject("currently").getDouble("apparentTemperature");
                            String summary =  response.getJSONObject("currently").getString("summary");
                            String region = response.getJSONArray("alerts").getJSONObject(0).getJSONArray("regions").getString(0);
                            double[] temps = getTemperatures(response);
                            Double humidity = response.getJSONObject("currently").getDouble("humidity");
                            int uvIndex = response.getJSONObject("currently").getInt("uvIndex");
                            int windDirection = response.getJSONObject("currently").getInt("windBearing");

                            Intent intent = new Intent(WeatherSplashActivity.this, WeatherHomeActivity.class);

                            putExtraValues(intent,mainTemp,apparentTemp,summary,region,temps,humidity,uvIndex,windDirection,response);

                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    Toast.makeText(WeatherSplashActivity.this,"An error occured during API connection",Toast.LENGTH_SHORT);
            }
        });
        jsonObjectRequest.setTag(TAG);
        queue.add(jsonObjectRequest);
    }

    //Puts extra values into intent and starts new activity
    private void putExtraValues(Intent intent, double mainTemp, double apparentTemp, String summary, String region, double[] temps, Double humidity, int uvindex, int windDirection, JSONObject response) throws JSONException{
        intent.putExtra("temp", mainTemp);
        intent.putExtra("region", region);
        intent.putExtra("tempMax",temps[0]);
        intent.putExtra("tempMin",temps[1]);
        intent.putExtra("summary", summary);
        intent.putExtra("apparentTemp", apparentTemp);
        intent.putExtra("humidity",humidity);
        intent.putExtra("uvindex",uvindex);
        intent.putExtra("windDirection",getWindDirection(windDirection));
        intent.putParcelableArrayListExtra("hourlyForecast", getHourlyForecast(response));
        intent.putParcelableArrayListExtra("weeklyForecast",getWeeklyForecast(response));
    }

    //gets wind direction from api response
    private String getWindDirection(int windDirection) {
        String[] directions = {"North","Northeast","East","Southeast","South","Southwest","West","Northwest"};
        int direction = (int) (Math.round((double)windDirection / 45) % 8);
        return directions[direction];
    }

    //gets all the data needed for hourly forecast
    private ArrayList<ForecastModel> getHourlyForecast(JSONObject response) throws JSONException {
        ArrayList<ForecastModel> hourlyForecast = new ArrayList<>();
        for(int i = 1;i<=24;i++)
        {
            hourlyForecast.add(new ForecastModel(response.getJSONObject("hourly").getJSONArray("data").getJSONObject(i).getDouble("temperature"),response.getJSONObject("hourly").getJSONArray("data").getJSONObject(i).getInt("time")));
        }
        return hourlyForecast;
    }

    //gets all the data needed for weekly forecast
    private ArrayList<ForecastModel> getWeeklyForecast(JSONObject response) throws JSONException{
        ArrayList<ForecastModel> weeklyForecast = new ArrayList<>();
        for(int i = 0;i<=7;i++){
            weeklyForecast.add(new ForecastModel(response.getJSONObject("daily").getJSONArray("data").getJSONObject(i).getInt("time"),response.getJSONObject("daily").getJSONArray("data").getJSONObject(i).getDouble("temperatureMax"),response.getJSONObject("daily").getJSONArray("data").getJSONObject(i).getDouble("temperatureMin")));
        }
        return weeklyForecast;
    }

    //gets minimal and maximum temperatures for the current day
    private double[] getTemperatures(JSONObject response) throws JSONException {
        double[] temps = new double[2];
        temps[0] = response.getJSONObject("daily").getJSONArray("data").getJSONObject(0).getDouble("temperatureMax");
        temps[1] = response.getJSONObject("daily").getJSONArray("data").getJSONObject(0).getDouble("temperatureMin");
//        long timeMs = response.getJSONObject("daily").getJSONArray("data").getJSONObject(i).getInt("time");
//        while(true){
//
//            if(!DateFormat.getDateInstance().format(new Date()).equals(DateFormat.getDateInstance().format(new Date(timeMs * 1000)))){
//                break;
//            }
//
//            ++i;
//            timeMs = response.getJSONObject("daily").getJSONArray("data").getJSONObject(i).getInt("time");
//            temps[0] = response.getJSONObject("daily").getJSONArray("data").getJSONObject(i).getDouble("temperatureMax");
//            temps[1] = response.getJSONObject("daily").getJSONArray("data").getJSONObject(i).getDouble("temperatureMin");
//        }

        return temps;
    }




}
