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
import com.weather.storm.cassandra.table.Milestone;
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

        // -- Temperature: Location - Year - Month - Average - Max - Min

        try {
            for (Object obj : tuple.getValues()) {
                if (obj instanceof Temperature) {
                    Temperature temperature = (Temperature) obj;

                    DateTime date = new DateTime(temperature.getMeasuredtime());
                    int year = date.getYear();
                    int month = date.getMonthOfYear();

                    // -- Get the current values
                    // -- Problem with running get too often is that there could be issues with buffer in cassandra driver

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
                        Milestone milestone = new Milestone(temperature.getStationid(), temperature.getMeasuredtime(),
                                temperature.getMeasurement());
                        monthlyStat = new MonthlyStat(temperature.getLocationid(), MEASUREMENT_ENTITY.TEMPERATURE.value, year,
                                month, 1, getMilestoneList(milestone), getMilestoneList(milestone), temperature.getMeasurement());
                    }
                    else {
                        float newAverage = CommonUtil.getRunningMean(monthlyStat.getAverage(), temperature.getMeasurement(),
                                monthlyStat.getCount() + 1);

                        Milestone newMax = null;
                        if (monthlyStat.getMax().get(0).getValue() > temperature.getMeasurement()) {
                            newMax = monthlyStat.getMax().get(0);
                        }
                        else {
                            newMax = new Milestone(temperature.getStationid(), temperature.getMeasuredtime(),
                                    temperature.getMeasurement());
                        }

                        Milestone newMin = null;
                        if (monthlyStat.getMin().get(0).getValue() < temperature.getMeasurement()) {
                            newMin = monthlyStat.getMin().get(0);
                        }
                        else {
                            newMin = new Milestone(temperature.getStationid(), temperature.getMeasuredtime(),
                                    temperature.getMeasurement());
                        }

                        monthlyStat.setCount(monthlyStat.getCount() + 1);
                        monthlyStat.setAverage(newAverage);
                        monthlyStat.setMax(getMilestoneList(newMax));
                        monthlyStat.setMin(getMilestoneList(newMax));
                    }

                    monthlyStat.setLocationid(temperature.getLocationid());
                    monthlyStat.setEntity(MEASUREMENT_ENTITY.TEMPERATURE.value);
                    monthlyStat.setYear(year);
                    monthlyStat.setMonth(month);

                    LOG.warn(monthlyStat.toString());
                    // monthlyStatMapper.save(monthlyStat);
                    // monthlyStatAccessor.updateMax1(monthlyStat.getMax().getStationid(), monthlyStat.getMax().getMeasuredtime(),
                    // monthlyStat.getMax().getValue(), monthlyStat.getLocationid(), monthlyStat.getEntity(), year, month);
                    // monthlyStatAccessor.updateMax(monthlyStat.getMax(), monthlyStat.getLocationid(),
                    // monthlyStat.getEntity(),
                    // year, month);

                    monthlyStatAccessor.add(temperature.getLocationid(), MEASUREMENT_ENTITY.TEMPERATURE.value, year, month,
                            monthlyStat.getCount(), monthlyStat.getAverage(), monthlyStat.getMax(), monthlyStat.getMin());

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

    private List<Milestone> getMilestoneList(Milestone milestone) {
        List<Milestone> list = new ArrayList<>();
        list.add(milestone);
        return list;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("temperature"));
    }
}
