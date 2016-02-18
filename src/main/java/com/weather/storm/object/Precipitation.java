package com.weather.storm.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("serial")
public class Precipitation extends BaseEntity {

    @SerializedName("Low")
    @Expose
    private float low;

    @SerializedName("High")
    @Expose
    private float high;

    public Precipitation(int locationId, int stationId, long timestamp, float low, float high) {
        super(locationId, stationId, timestamp);
        this.low = low;
        this.high = high;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    @Override
    public String toString() {
        return super.toString() + " Low: " + low + " High: " + high;
    }
}
