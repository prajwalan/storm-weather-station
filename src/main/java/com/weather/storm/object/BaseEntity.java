package com.weather.storm.object;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("serial")
public abstract class BaseEntity implements Serializable {
    @SerializedName("LocationId")
    @Expose
    private int locationId;

    @SerializedName("StationId")
    @Expose
    private int stationId;

    @SerializedName("Timestamp")
    @Expose
    private long timestamp;

    public BaseEntity(int locationId, int stationId, long timestamp) {
        super();
        this.locationId = locationId;
        this.stationId = stationId;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Location: " + locationId + " Station: " + stationId + " Timestamp: " + timestamp;
    }
}
