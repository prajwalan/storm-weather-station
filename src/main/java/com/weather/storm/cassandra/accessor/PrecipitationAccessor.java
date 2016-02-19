package com.weather.storm.cassandra.accessor;

import java.util.Date;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;

@Accessor
public interface PrecipitationAccessor {

    @Query("INSERT INTO precipitation(locationid, stationid, measuredtime, low, high)"
            + "VALUES(:locationid, :stationid, :measuredtime, :low, :high)")
    public ResultSet add( //
            @Param("locationid") int locationid, //
            @Param("stationid") int stationid, //
            @Param("measuredtime") Date measuredtime, //
            @Param("low") float low, //
            @Param("high") float high //
    );

}
