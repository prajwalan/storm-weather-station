package com.weather.storm.bolt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.mapping.Mapper;
import com.weather.storm.cassandra.accessor.MonthlyStatAccessor;
import com.weather.storm.cassandra.table.MonthlyStat;
import com.weather.storm.cassandra.table.Temperature;
import com.weather.storm.util.CommonUtil;
import com.weather.storm.env.Constants.MEASUREMENT_ENTITY;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

@SuppressWarnings("serial")
public class TemperatureStatsBolt extends BaseCassandraBolt {

    private static final Logger LOG = LoggerFactory.getLogger(TemperatureStatsBolt.class);

    private HashMap<String, MonthlyStat> tempCache;
    private MonthlyStatAccessor monthlyStatAccessor;

    public TemperatureStatsBolt(String host, int port, String keyspace) {
        super(host, port, keyspace);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        super.prepare(stormConf, context, collector);

        monthlyStatAccessor = manager.createAccessor(MonthlyStatAccessor.class);
        tempCache = new HashMap<>();
    }

    @Override
    public void execute(Tuple tuple) {

        // -- Temperature: Location - Year - Month - Average - Count - Max - Min

        try {
            for (Object obj : tuple.getValues()) {
                if (obj instanceof Temperature) {
                    Temperature temperature = (Temperature) obj;

                    DateTime date = new DateTime(temperature.getMeasuredtime());
                    int year = date.getYear();
                    int month = date.getMonthOfYear();

                    // -- Get the current values
                    // -- Problem with running get too often is that there could be issues with buffer in Cassandra driver
                    MonthlyStat monthlyStat = null;

                    String key = "" + temperature.getLocationid() + MEASUREMENT_ENTITY.TEMPERATURE.value + year + month;
                    if (tempCache.containsKey(key)) {
                        monthlyStat = tempCache.get(key);
                    }
                    else {
                        monthlyStat = monthlyStatAccessor.get(temperature.getLocationid(), MEASUREMENT_ENTITY.TEMPERATURE.value,
                                year, month);
                    }

                    if (monthlyStat == null) {
                        monthlyStat = new MonthlyStat(temperature.getLocationid(), MEASUREMENT_ENTITY.TEMPERATURE.value, year,
                                month, temperature.getMeasurement(), 1, //
                                temperature.getMeasurement(), temperature.getStationid(), date.toDate(), //
                                temperature.getMeasurement(), temperature.getStationid(), date.toDate());
                    }
                    else {
                        float newAverage = CommonUtil.getRunningMean(monthlyStat.getAverageNumeric(),
                                temperature.getMeasurementNumeric(), monthlyStat.getCount() + 1);

                        monthlyStat.setCount(monthlyStat.getCount() + 1);
                        monthlyStat.setAverageNumeric(newAverage);
                        if (temperature.getMeasurementNumeric() > monthlyStat.getMaxNumeric()) {
                            monthlyStat.setMax(temperature.getMeasurement());
                            monthlyStat.setMaxstationid(temperature.getStationid());
                            monthlyStat.setMaxtime(temperature.getMeasuredtime());
                        }
                        if (temperature.getMeasurementNumeric() < monthlyStat.getMinNumeric()) {
                            monthlyStat.setMin(temperature.getMeasurement());
                            monthlyStat.setMinstationid(temperature.getStationid());
                            monthlyStat.setMintime(temperature.getMeasuredtime());
                        }
                    }

                    monthlyStat.setLocationid(temperature.getLocationid());
                    monthlyStat.setEntity(MEASUREMENT_ENTITY.TEMPERATURE.value);
                    monthlyStat.setYear(year);
                    monthlyStat.setMonth(month);

                    monthlyStatAccessor.add(temperature.getLocationid(), MEASUREMENT_ENTITY.TEMPERATURE.value, year, month, //
                            monthlyStat.getAverage(), monthlyStat.getCount(), //
                            monthlyStat.getMax(), monthlyStat.getMaxstationid(), monthlyStat.getMaxtime(), //
                            monthlyStat.getMin(), monthlyStat.getMinstationid(), monthlyStat.getMintime());

                    tempCache.put(key, monthlyStat);
                }
                else {
                    LOG.warn("Non temperature message received");
                }

            }
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }

        collector.ack(tuple);

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("temperature"));
    }
}
