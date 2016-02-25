package com.weather.storm.cassandra.accessor;

import java.util.List;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import com.weather.storm.cassandra.table.Milestone;
import com.weather.storm.cassandra.table.MonthlyStat;

@Accessor
public interface MonthlyStatAccessor {

    @Query("SELECT locationid, entity, year, month, count, average, max, min FROM monthlystat "
            + "where locationid=:locationid and entity=:entity")
    public Result<MonthlyStat> get(@Param("locationid") int locationid, @Param("entity") int entity);

    @Query("SELECT locationid, entity, year, month, count, average, max, min FROM monthlystat "
            + "where locationid=:locationid and entity=:entity and year=:year and month=:month")
    public MonthlyStat get(@Param("locationid") int locationid, @Param("entity") int entity, @Param("year") int year,
            @Param("month") int month);

    @Query("INSERT INTO monthlystat(locationid, entity, year, month, count, average, max, min)"
            + "VALUES(:locationid, :entity, :year, :month, :count, :average, :max, :min)")
    public ResultSet add( //
            @Param("locationid") int locationid, //
            @Param("entity") int entity, //
            @Param("year") int year, //
            @Param("month") int month, //
            @Param("count") int count, //
            @Param("average") float average, //
            @Param("max") List<Milestone> max, //
            @Param("min") List<Milestone> min //
    );

}
