package com.weather.storm.cassandra.table;

import java.io.Serializable;
import java.util.Date;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.weather.storm.env.EnvConstant;

@SuppressWarnings("serial")
@Table(keyspace = EnvConstant.CASSANDRA_KEYSPACE, name = "precipitation")
public class Precipitation implements Serializable {

    @PartitionKey(0)
    private int locationid;
    @PartitionKey(1)
    private int stationid;
    private Date measuredtime;
    private float low;
    private float high;

    public Precipitation() {

    }

    public Precipitation(int locationid, int stationid, Date measuredtime, float low, float high) {
        super();
        this.locationid = locationid;
        this.stationid = stationid;
        this.measuredtime = measuredtime;
        this.low = low;
        this.high = high;
    }

    public int getLocationid() {
        return locationid;
    }

    public void setLocationid(int locationid) {
        this.locationid = locationid;
    }

    public int getStationid() {
        return stationid;
    }

    public void setStationid(int stationid) {
        this.stationid = stationid;
    }

    public Date getMeasuredtime() {
        return measuredtime;
    }

    public void setMeasuredtime(Date measuredtime) {
        this.measuredtime = measuredtime;
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

}
