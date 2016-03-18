package com.weather.storm.cassandra.table;

import java.io.Serializable;
import java.util.Date;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.weather.storm.env.EnvConstant;

@SuppressWarnings("serial")
@Table(keyspace = EnvConstant.CASSANDRA_KEYSPACE, name = "temperature")
public class Temperature implements Serializable {

    @PartitionKey(0)
    private int locationid;
    @PartitionKey(1)
    private int stationid;
    private Date measuredtime;
    private String measurement;

    public Temperature() {

    }

    public Temperature(int locationid, int stationid, Date measuredtime, String measurement) {
        super();
        this.locationid = locationid;
        this.stationid = stationid;
        this.measuredtime = measuredtime;
        this.measurement = measurement;
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

    public String getMeasurement() {
        return measurement;
    }

    public float getMeasurementNumeric() {
        return Float.parseFloat(measurement);
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

}
