package com.weather.storm.cassandra.table;

import java.util.Date;

import com.datastax.driver.mapping.annotations.UDT;
import com.weather.storm.env.EnvConstant;

@UDT(keyspace = EnvConstant.CASSANDRA_KEYSPACE, name = "milestone")
public class Milestone {

    private int stationid;
    private Date measuredtime;
    private float value;

    public Milestone() {
    }

    public Milestone(int stationid, Date measuredtime, float value) {
        super();
        this.stationid = stationid;
        this.measuredtime = measuredtime;
        this.value = value;
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

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "[Station: " + stationid + " time: " + measuredtime.getTime() + " value: " + value + "]";
    }
}
