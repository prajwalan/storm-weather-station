package com.weather.storm.object;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("serial")
public class Temperature implements Serializable {

    @SerializedName("LocationId")
    @Expose
    private int locationId;

    @SerializedName("StationId")
    @Expose
    private int stationId;

    @SerializedName("Measurement")
    @Expose
    private String measurement;

    @SerializedName("Timestamp")
    @Expose
    private long timestamp;

    public Temperature() {

    }

    public Temperature(int locationId, int stationId, String measurement, long timestamp) {
        super();
        this.locationId = locationId;
        this.stationId = stationId;
        this.measurement = measurement;
        this.timestamp = timestamp;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Location: " + locationId + " Station: " + stationId + " Measurement: " + measurement + " Timestamp: " + timestamp;
    }
}
