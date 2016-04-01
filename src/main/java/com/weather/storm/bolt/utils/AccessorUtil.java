package com.weather.storm.bolt.utils;

import com.datastax.driver.mapping.MappingManager;
import com.weather.storm.cassandra.accessor.MonthlyStatAccessor;
import com.weather.storm.cassandra.accessor.TemperatureAccessor;

public class AccessorUtil {

    public static TemperatureAccessor createTemperatureAccessor(MappingManager manager) {
        return manager.createAccessor(TemperatureAccessor.class);
    }

    public static MonthlyStatAccessor createMonthlyStatAccessor(MappingManager manager) {
        return manager.createAccessor(MonthlyStatAccessor.class);
    }

}
