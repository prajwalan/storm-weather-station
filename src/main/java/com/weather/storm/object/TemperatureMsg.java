package com.weather.storm.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("serial")
public class TemperatureMsg extends BaseEntity {

    @SerializedName("Measurement")
    @Expose
    private String measurement;

    public TemperatureMsg(int locationId, int stationId, long timestamp, String measurement) {
        super(locationId, stationId, timestamp);
        this.measurement = measurement;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    @Override
    public String toString() {
        return super.toString() + " Measurement: " + measurement;
    }
}
