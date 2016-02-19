package com.weather.storm.cassandra.table;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.weather.storm.env.EnvConstant;

@Table(keyspace = EnvConstant.CASSANDRA_KEYSPACE, name = "monthlystat")
public class MonthlyStat {

    @PartitionKey(0)
    private int locationid;
    @PartitionKey(1)
    private int entity;
    private int year;
    private int month;
    private int count;
    private float max;
    private float min;
    private float average;

    public MonthlyStat() {

    }

    public MonthlyStat(int locationid, int entity, int year, int month, int count, float max, float min, float average) {
        super();
        this.locationid = locationid;
        this.entity = entity;
        this.year = year;
        this.month = month;
        this.count = count;
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
