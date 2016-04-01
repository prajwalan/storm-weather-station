package com.weather.storm.bolt.utils;

import java.util.Date;

import com.datastax.driver.core.ResultSet;
import com.weather.storm.cassandra.accessor.TemperatureAccessor;
import com.weather.storm.object.TemperatureMsg;

public class StorageBoltUtil {

    public static ResultSet storeInDatabase(TemperatureAccessor temperatureAccessor, TemperatureMsg tempMsg) {
        return temperatureAccessor.add(tempMsg.getLocationId(), tempMsg.getStationId(), new Date(tempMsg.getTimestamp()),
                tempMsg.getMeasurement());
    }
}
