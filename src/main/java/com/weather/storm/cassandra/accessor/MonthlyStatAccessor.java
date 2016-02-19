package com.weather.storm.cassandra.accessor;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import com.weather.storm.cassandra.table.MonthlyStat;

@Accessor
public interface MonthlyStatAccessor {

    @Query("SELECT locationid, entity, year, month, average, max, min FROM monthlystat where locationid=:locationid and entity=:entity")
    public Result<MonthlyStat> get(@Param("locationid") int locationid, @Param("entity") int entity);

    @Query("INSERT INTO monthlystat(locationid, entity, year, month, average, max, min)"
            + "VALUES(:locationid, :entity, :year, :month, :average, :max, :min)")
    public ResultSet add( //
            @Param("locationid") int locationid, //
            @Param("entity") int entity, //
            @Param("year") int year, //
            @Param("month") int month, //
            @Param("average") float average, //
            @Param("max") float max, //
            @Param("min") float min //
    );
}
