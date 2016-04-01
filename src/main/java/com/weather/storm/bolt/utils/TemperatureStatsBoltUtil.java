package com.weather.storm.bolt.utils;

import org.joda.time.DateTime;

import com.datastax.driver.core.ResultSet;
import com.weather.storm.cassandra.accessor.MonthlyStatAccessor;
import com.weather.storm.cassandra.table.MonthlyStat;
import com.weather.storm.cassandra.table.Temperature;
import com.weather.storm.env.Constants.MEASUREMENT_ENTITY;

public class TemperatureStatsBoltUtil {

    public static MonthlyStat getFromDatabase(MonthlyStatAccessor monthlyStatAccessor, int locationid, int year, int month) {
        return monthlyStatAccessor.get(locationid, MEASUREMENT_ENTITY.TEMPERATURE.value, year, month);
    }

    public static ResultSet storeInDatabase(MonthlyStatAccessor monthlyStatAccessor, Temperature temperature,
            MonthlyStat monthlyStat) {
        DateTime date = new DateTime(temperature.getMeasuredtime());
        return monthlyStatAccessor.add(temperature.getLocationid(), MEASUREMENT_ENTITY.TEMPERATURE.value, date.getYear(),
                date.getMonthOfYear(), //
                monthlyStat.getAverage(), monthlyStat.getCount(), //
                monthlyStat.getMax(), monthlyStat.getMaxstationid(), monthlyStat.getMaxtime(), //
                monthlyStat.getMin(), monthlyStat.getMinstationid(), monthlyStat.getMintime());
    }

}
