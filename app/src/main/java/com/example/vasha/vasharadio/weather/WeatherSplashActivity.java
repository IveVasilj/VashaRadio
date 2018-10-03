package com.example.vasha.vasharadio.weather;

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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

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

    @SuppressLint("MissingPermission")
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://api.darksky.net/forecast/0ad19f6ac2ec3942d4300b750576a445/" + latitude + "," + longitude + "?units=auto", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            double mainTemp = response.getJSONObject("currently").getDouble("temperature");
                            double apparentTemp = response.getJSONObject("currently").getDouble("apparentTemperature");
                            String summary =  response.getJSONObject("currently").getString("summary");
                            String region = response.getJSONArray("alerts").getJSONObject(0).getJSONArray("regions").getString(0);
                            double[] temps = getTemperatures(response);

                            Intent intent = new Intent(WeatherSplashActivity.this, WeatherHomeActivity.class);
                            intent.putExtra("temp", mainTemp);
                            intent.putExtra("region", region);
                            intent.putExtra("tempMax",temps[0]);
                            intent.putExtra("tempMin",temps[1]);
                            intent.putExtra("summary", summary);
                            intent.putExtra("apparentTemp", apparentTemp);

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

    private double[] getTemperatures(JSONObject response) throws JSONException {
        double[] temps = new double[2];
        int i = 0;
        temps[0] = response.getJSONObject("daily").getJSONArray("data").getJSONObject(i).getDouble("temperatureHigh");
        temps[1] = response.getJSONObject("daily").getJSONArray("data").getJSONObject(i).getDouble("temperatureLow");
        long timeMs = response.getJSONObject("daily").getJSONArray("data").getJSONObject(i).getInt("time");
        while(true){

            if(!DateFormat.getDateInstance().format(new Date()).equals(DateFormat.getDateInstance().format(new Date(timeMs * 1000)))){
                break;
            }

            ++i;
            timeMs = response.getJSONObject("daily").getJSONArray("data").getJSONObject(i).getInt("time");
            temps[0] = response.getJSONObject("daily").getJSONArray("data").getJSONObject(i).getDouble("temperatureHigh");
            temps[1] = response.getJSONObject("daily").getJSONArray("data").getJSONObject(i).getDouble("temperatureLow");
        }

        return temps;
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

            }
        }
    }
}
