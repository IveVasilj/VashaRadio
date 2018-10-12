package com.example.vasha.vasharadio.weather.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ForecastModel implements Parcelable{
    private double currentTemp;
    private double tempMax;
    private double tempMin;
    private long epochTimestamp;
    private String weatherIcon;

    public ForecastModel(double currentTemp, long epochTimestamp, String weatherIcon){
        this.epochTimestamp = epochTimestamp;
        this.currentTemp = currentTemp;
        this.weatherIcon = weatherIcon;
    }

    public ForecastModel(int epochTimestamp, double tempMax, double tempMin, String weatherIcon){
        this.epochTimestamp = epochTimestamp;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.weatherIcon = weatherIcon;
    }

    public double getCurrentTemp() {
        return currentTemp;
    }

    public double getTempMax() {
        return tempMax;
    }

    public double getTempMin() {
        return tempMin;
    }

    public long getEpochTimestamp() {
        return epochTimestamp;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(currentTemp);
        dest.writeDouble(tempMax);
        dest.writeDouble(tempMin);
        dest.writeLong(epochTimestamp);
        dest.writeString(weatherIcon);
    }

    public static final Parcelable.Creator<ForecastModel> CREATOR = new Parcelable.Creator<ForecastModel>(){
        @Override
        public ForecastModel[] newArray(int size) {
            return new ForecastModel[size];
        }

        @Override
        public ForecastModel createFromParcel(Parcel source) {
            return new ForecastModel(source);
        }
    };

    private ForecastModel(Parcel source){
        currentTemp = source.readDouble();
        tempMax = source.readDouble();
        tempMin = source.readDouble();
        epochTimestamp = source.readLong();
        weatherIcon = source.readString();
    }
}
