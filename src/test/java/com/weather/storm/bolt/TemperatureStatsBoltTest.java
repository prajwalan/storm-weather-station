package com.weather.storm.bolt;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.weather.storm.cassandra.table.MonthlyStat;
import com.weather.storm.cassandra.table.Temperature;

public class TemperatureStatsBoltTest {

    /**
     * Testing readings from different stations that belong to same location
     */
    @Test
    public void testCalculateMonthlyStats() {

        TemperatureStatsBolt bolt = new TemperatureStatsBolt("", 0, "");

        int locationid = 1;
        int stationid = 20;
        Date measuredtime = new Date();
        String measurement = "4.55";

        MonthlyStat monthlyStat = null;

        monthlyStat = bolt.calculateMonthlyStats(new Temperature(locationid, stationid, measuredtime, measurement), monthlyStat);
        Assert.assertNotNull(monthlyStat);
        Assert.assertEquals("4.55", monthlyStat.getAverage());
        Assert.assertEquals("4.55", monthlyStat.getMax());
        Assert.assertEquals("4.55", monthlyStat.getMin());
        Assert.assertEquals(20, monthlyStat.getMaxstationid());
        Assert.assertEquals(20, monthlyStat.getMinstationid());

        stationid = 21;
        measuredtime = new Date();
        measurement = "7.05";
        monthlyStat = bolt.calculateMonthlyStats(new Temperature(locationid, stationid, measuredtime, measurement), monthlyStat);
        Assert.assertEquals("5.8", monthlyStat.getAverage());
        Assert.assertEquals("7.05", monthlyStat.getMax());
        Assert.assertEquals("4.55", monthlyStat.getMin());
        Assert.assertEquals(21, monthlyStat.getMaxstationid());
        Assert.assertEquals(20, monthlyStat.getMinstationid());

        stationid = 22;
        measuredtime = new Date();
        measurement = "6.31";
        monthlyStat = bolt.calculateMonthlyStats(new Temperature(locationid, stationid, measuredtime, measurement), monthlyStat);
        Assert.assertEquals("5.97", monthlyStat.getAverage());
        Assert.assertEquals("7.05", monthlyStat.getMax());
        Assert.assertEquals("4.55", monthlyStat.getMin());
        Assert.assertEquals(21, monthlyStat.getMaxstationid());
        Assert.assertEquals(20, monthlyStat.getMinstationid());
    }

    /**
     * Testing readings from different locations
     */
    @Test
    public void testCalculateMonthlyStats2() {

        TemperatureStatsBolt bolt = new TemperatureStatsBolt("", 0, "");

        int locationid = 1;
        int stationid = 20;
        Date measuredtime = new Date();
        String measurement = "4.55";

        MonthlyStat monthlyStat_loc1 = null;
        MonthlyStat monthlyStat_loc2 = null;

        monthlyStat_loc1 = bolt.calculateMonthlyStats(new Temperature(locationid, stationid, measuredtime, measurement),
                monthlyStat_loc1);
        Assert.assertNotNull(monthlyStat_loc1);
        Assert.assertEquals("4.55", monthlyStat_loc1.getAverage());
        Assert.assertEquals("4.55", monthlyStat_loc1.getMax());
        Assert.assertEquals("4.55", monthlyStat_loc1.getMin());
        Assert.assertEquals(20, monthlyStat_loc1.getMaxstationid());
        Assert.assertEquals(20, monthlyStat_loc1.getMinstationid());

        locationid = 2;
        stationid = 21;
        measuredtime = new Date();
        measurement = "7.05";
        monthlyStat_loc2 = bolt.calculateMonthlyStats(new Temperature(locationid, stationid, measuredtime, measurement),
                monthlyStat_loc2);
        Assert.assertEquals("7.05", monthlyStat_loc2.getAverage());
        Assert.assertEquals("7.05", monthlyStat_loc2.getMax());
        Assert.assertEquals("7.05", monthlyStat_loc2.getMin());
        Assert.assertEquals(21, monthlyStat_loc2.getMaxstationid());
        Assert.assertEquals(21, monthlyStat_loc2.getMinstationid());

    }
}
