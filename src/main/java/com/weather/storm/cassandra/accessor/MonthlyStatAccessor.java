package com.weather.storm.cassandra.accessor;

import java.util.Date;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import com.weather.storm.cassandra.table.MonthlyStat;

@Accessor
public interface MonthlyStatAccessor {

    @Query("SELECT locationid, entity, year, month, average, count, max, maxstationid, maxtime, min, minstationid, mintime FROM weather_station_keyspace.monthlystat "
            + "where locationid=:locationid and entity=:entity")
    public Result<MonthlyStat> get(@Param("locationid") int locationid, @Param("entity") int entity);

    @Query("SELECT locationid, entity, year, month, average, count, max, maxstationid, maxtime, min, minstationid, mintime FROM weather_station_keyspace.monthlystat "
            + "where locationid=:locationid and entity=:entity and year=:year and month=:month")
    public MonthlyStat get(@Param("locationid") int locationid, @Param("entity") int entity, @Param("year") int year,
            @Param("month") int month);

    @Query("INSERT INTO weather_station_keyspace.monthlystat (locationid, entity, year, month, average, count, max, maxstationid, maxtime, min, minstationid, mintime)"
            + "VALUES(:locationid, :entity, :year, :month, :average, :count, :max, :maxstationid, :maxtime, :min, :minstationid, :mintime)")
    public ResultSet add( //
            @Param("locationid") int locationid, //
            @Param("entity") int entity, //
            @Param("year") int year, //
            @Param("month") int month, //
            @Param("average") String average, //
            @Param("count") int count, //
            @Param("max") String max, //
            @Param("maxstationid") int maxstationid, //
            @Param("maxtime") Date maxtime, //
            @Param("min") String min, //
            @Param("minstationid") int minstationid, //
            @Param("mintime") Date mintime);

}
