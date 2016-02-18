package com.weather.storm.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("serial")
public class Temperature extends BaseEntity {

    @SerializedName("Measurement")
    @Expose
    private float measurement;

    public Temperature(int locationId, int stationId, long timestamp, float measurement) {
        super(locationId, stationId, timestamp);
        this.measurement = measurement;
    }

    public float getMeasurement() {
        return measurement;
    }

    public void setMeasurement(float measurement) {
        this.measurement = measurement;
    }

    @Override
    public String toString() {
        return super.toString() + " Measurement: " + measurement;
    }
}
