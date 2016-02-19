package com.weather.storm.cassandra.table;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.weather.storm.env.EnvConstant;

@Table(keyspace = EnvConstant.CASSANDRA_KEYSPACE, name = "weeklystat")
public class WeeklyStat {

    @PartitionKey(0)
    private int locationid;
    @PartitionKey(1)
    private int entity;
    private int year;
    private int week;
    private float max;
    private float min;
    private float average;

    public WeeklyStat() {

    }

    public WeeklyStat(int locationid, int entity, int year, int week, float max, float min, float average) {
        super();
        this.locationid = locationid;
        this.entity = entity;
        this.year = year;
        this.week = week;
        this.max = max;
        this.min = min;
        this.average = average;
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

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

}
