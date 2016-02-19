package com.weather.storm.cassandra.accessor;

import java.util.Date;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;

@Accessor
public interface TemperatureAccessor {

    @Query("INSERT INTO temperature(locationid, stationid, measuredtime, measurement)"
            + "VALUES(:locationid, :stationid, :measuredtime, :measurement)")
    public ResultSet add( //
            @Param("locationid") int locationid, //
            @Param("stationid") int stationid, //
            @Param("measuredtime") Date measuredtime, //
            @Param("measurement") float measurement //
    );
}
