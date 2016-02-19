package com.weather.storm.bolt;

import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private MonthlyStatAccessor monthlyStatAccessor;

    public TemperatureStatsBolt(String host, int port, String keyspace) {
        super(host, port, keyspace);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        super.prepare(stormConf, context, collector);

        monthlyStatAccessor = manager.createAccessor(MonthlyStatAccessor.class);
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
                    MonthlyStat monthlyStat = monthlyStatAccessor.get(temperature.getLocationid(),
                            MEASUREMENT_ENTITY.TEMPERATURE.value, year, month);
                    if (monthlyStat == null) {
                        monthlyStat = new MonthlyStat(temperature.getLocationid(), MEASUREMENT_ENTITY.TEMPERATURE.value, year,
                                month, 1, temperature.getMeasurement(), temperature.getMeasurement(),
                                temperature.getMeasurement());
                    }
                    else {
                        float newMax = monthlyStat.getMax() > temperature.getMeasurement() ? monthlyStat.getMax()
                                : temperature.getMeasurement();
                        float newMin = monthlyStat.getMin() < temperature.getMeasurement() ? monthlyStat.getMin()
                                : temperature.getMeasurement();
                        float newAverage = CommonUtil.getRunningMean(monthlyStat.getAverage(), temperature.getMeasurement(),
                                monthlyStat.getCount() + 1);

                        monthlyStat.setCount(monthlyStat.getCount() + 1);
                        monthlyStat.setAverage(newAverage);
                        monthlyStat.setMax(newMax);
                        monthlyStat.setMin(newMin);
                    }

                    monthlyStatAccessor.add(temperature.getLocationid(), MEASUREMENT_ENTITY.TEMPERATURE.value, year, month,
                            monthlyStat.getCount(), monthlyStat.getAverage(), monthlyStat.getMax(), monthlyStat.getMin());
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
