package com.weather.storm.cassandra.table;

import java.util.Date;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.weather.storm.env.EnvConstant;
import com.weather.storm.util.CommonUtil;

@Table(keyspace = EnvConstant.CASSANDRA_KEYSPACE, name = "monthlystat")
public class MonthlyStat {

    @PartitionKey(0)
    private int locationid;
    @PartitionKey(1)
    private int entity;
    private int year;
    private int month;
    private String average;
    private int count;
    private String max;
    private int maxstationid;
    private Date maxtime;
    private String min;
    private int minstationid;
    private Date mintime;

    public MonthlyStat() {
        count = 0;
        average = "0.0";
        max = "0.0";
        min = "0.0";
    }

    public MonthlyStat(int locationid, int entity, int year, int month, String average, int count, String max, int maxstationid,
            Date maxtime, String min, int minstationid, Date mintime) {
        super();
        this.locationid = locationid;
        this.entity = entity;
        this.year = year;
        this.month = month;
        this.average = average;
        this.count = count;
        this.max = max;
        this.maxstationid = maxstationid;
        this.maxtime = maxtime;
        this.min = min;
        this.minstationid = minstationid;
        this.mintime = mintime;
    }

    public int getLocationid() {
        return locationid;
    }

    public void setLocationid(int locationid) {
        this.locationid = locationid;
    }

    public int getEntity() {
        return entity;
    }

    public void setEntity(int entity) {
        this.entity = entity;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAverage() {
        return average;
    }

    public float getAverageNumeric() {
        return Float.parseFloat(average);
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public void setAverageNumeric(float average) {
        this.average = "" + CommonUtil.round(average, 2);
    }

    public String getMax() {
        return max;
    }

    public float getMaxNumeric() {
        return Float.parseFloat(max);
    }

    public void setMax(String max) {
        this.max = max;
    }

    public void setMaxNumeric(float max) {
        this.max = "" + CommonUtil.round(max, 2);
    }

    public int getMaxstationid() {
        return maxstationid;
    }

    public void setMaxstationid(int maxstationid) {
        this.maxstationid = maxstationid;
    }

    public Date getMaxtime() {
        return maxtime;
    }

    public void setMaxtime(Date maxtime) {
        this.maxtime = maxtime;
    }

    public String getMin() {
        return min;
    }

    public float getMinNumeric() {
        return Float.parseFloat(min);
    }

    public void setMin(String min) {
        this.min = min;
    }

    public void setMinNumeric(float min) {
        this.min = "" + CommonUtil.round(min, 2);
    }

    public int getMinstationid() {
        return minstationid;
    }

    public void setMinstationid(int minstationid) {
        this.minstationid = minstationid;
    }

    public Date getMintime() {
        return mintime;
    }

    public void setMintime(Date mintime) {
        this.mintime = mintime;
    }

    @Override
    public String toString() {
        return "Location: " + locationid + " year: " + year + " month: " + month + " max: " + max + " min: " + min;
    }

}
