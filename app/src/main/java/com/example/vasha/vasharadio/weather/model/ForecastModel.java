package com.example.vasha.vasharadio.weather.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ForecastModel implements Parcelable{
    private double currentTemp;
    private double tempMax;
    private double tempMin;
    private long epochTimestamp;

    public ForecastModel(double currentTemp, long epochTimestamp){
        this.epochTimestamp = epochTimestamp;
        this.currentTemp = currentTemp;
    }

    public ForecastModel(int epochTimestamp, double tempMax, double tempMin){
        this.epochTimestamp = epochTimestamp;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
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
    }
}
