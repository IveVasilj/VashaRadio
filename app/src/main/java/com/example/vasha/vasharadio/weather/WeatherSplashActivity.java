package com.example.vasha.vasharadio.weather;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.vasha.vasharadio.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


import org.json.JSONException;
import org.json.JSONObject;

public class WeatherSplashActivity extends AppCompatActivity{

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;

    private FusedLocationProviderClient mFusedLocationClient;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_splash);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(WeatherSplashActivity.this);
        queue = Volley.newRequestQueue(this);

        checkUserPermission();
    }

    private void checkUserPermission() {
        if (ContextCompat.checkSelfPermission(WeatherSplashActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(WeatherSplashActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions(WeatherSplashActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            } else {
                ActivityCompat.requestPermissions(WeatherSplashActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            }
        } else {
            getLastLocation();
        }
    }

    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            apiRequest(location.getLatitude(), location.getLongitude());
                        }
                        else{
                            System.out.print("error");
                        }
                    }
                });
    }

    private void apiRequest(double latitude, double longitude) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&units=metric&appid=51e8815b3da5517f14cb39b05d2cf9ac", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String main = response.getJSONArray("weather").getJSONObject(0).getString("main");
                            int currentTemp = response.getJSONObject("main").getInt("temp");
                            int tempMin = response.getJSONObject("main").getInt("temp_min");
                            int tempMax = response.getJSONObject("main").getInt("temp_max");
                            String city = response.getString("name");
                            String country = response.getJSONObject("sys").getString("country");

                            Intent intent = new Intent(WeatherSplashActivity.this, WeatherHomeActivity.class);
                            intent.putExtra("main", main);
                            intent.putExtra("currentTemp", currentTemp);
                            intent.putExtra("tempMin", tempMin);
                            intent.putExtra("tempMax", tempMax);
                            intent.putExtra("city", city);
                            intent.putExtra("country", country);

                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation();
                } else {
                    Toast.makeText(WeatherSplashActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }
}
