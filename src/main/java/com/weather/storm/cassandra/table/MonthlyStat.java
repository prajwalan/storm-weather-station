package com.weather.storm.cassandra.table;

import java.util.List;

import com.datastax.driver.mapping.annotations.Frozen;
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
    @Frozen("list <frozen<Milestone>>")
    private List<Milestone> max;
    @Frozen("list <frozen<Milestone>>")
    private List<Milestone> min;
    private float average;

    public MonthlyStat() {

    }

    public MonthlyStat(int locationid, int entity, int year, int month, int count, List<Milestone> max, List<Milestone> min,
            float average) {
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

    public List<Milestone> getMax() {
        return max;
    }

    public void setMax(List<Milestone> max) {
        this.max = max;
    }

    public List<Milestone> getMin() {
        return min;
    }

    public void setMin(List<Milestone> min) {
        this.min = min;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    @Override
    public String toString() {
        return "Location: " + locationid + " year: " + year + " month: " + month + " max: " + max + " min: " + min;
    }

}
