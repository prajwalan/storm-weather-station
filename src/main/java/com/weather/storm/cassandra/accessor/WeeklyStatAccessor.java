package com.weather.storm.cassandra.accessor;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import com.weather.storm.cassandra.table.WeeklyStat;

@Accessor
public interface WeeklyStatAccessor {

    @Query("SELECT locationid, entity, year, week, average, max, min FROM weeklystat where locationid=:locationid and entity=:entity")
    public Result<WeeklyStat> get(@Param("locationid") int locationid, @Param("entity") int entity);

    @Query("INSERT INTO weeklystat(locationid, entity, year, week, average, max, min)"
            + "VALUES(:locationid, :entity, :year, :week, :average, :max, :min)")
    public ResultSet add( //
            @Param("locationid") int locationid, //
            @Param("entity") int entity, //
            @Param("year") int year, //
            @Param("week") int week, //
            @Param("average") float average, //
            @Param("max") float max, //
            @Param("min") float min //
    );
}
