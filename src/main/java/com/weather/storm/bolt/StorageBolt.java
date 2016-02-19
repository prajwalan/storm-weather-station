package com.weather.storm.bolt;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.weather.storm.cassandra.accessor.PrecipitationAccessor;
import com.weather.storm.cassandra.accessor.TemperatureAccessor;
import com.weather.storm.cassandra.table.Precipitation;
import com.weather.storm.cassandra.table.Temperature;
import com.weather.storm.object.PrecipitationMsg;
import com.weather.storm.object.TemperatureMsg;
import com.weather.storm.util.CommonUtil;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

@SuppressWarnings("serial")
public class StorageBolt extends BaseCassandraBolt {

    private static final Logger LOG = LoggerFactory.getLogger(StorageBolt.class);

    private TemperatureAccessor temperatureAccessor;
    private PrecipitationAccessor precipitationAccessor;

    private Gson jsonConverter;

    public StorageBolt(String host, int port, String keyspace) {
        super(host, port, keyspace);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        super.prepare(stormConf, context, collector);

        temperatureAccessor = manager.createAccessor(TemperatureAccessor.class);
        precipitationAccessor = manager.createAccessor(PrecipitationAccessor.class);

        jsonConverter = CommonUtil.createJsonConvertor();
    }

    @Override
    public void execute(Tuple tuple) {

        try {
            for (Object msg : tuple.getValues()) {

                if (msg instanceof TemperatureMsg) {
                    TemperatureMsg tempMsg = (TemperatureMsg) msg;
                    Date measuredtime = new Date(tempMsg.getTimestamp());
                    Temperature temperature = new Temperature(tempMsg.getLocationId(), tempMsg.getStationId(), measuredtime,
                            tempMsg.getMeasurement());

                    temperatureAccessor.add(tempMsg.getLocationId(), tempMsg.getStationId(), measuredtime,
                            tempMsg.getMeasurement());

                    LOG.info("Saved and emitting temperature: " + jsonConverter.toJson(temperature).replaceAll("\n", ""));
                    collector.emit(tuple, new Values(temperature));
                }
                else if (msg instanceof PrecipitationMsg) {
                    PrecipitationMsg precipMsg = (PrecipitationMsg) msg;
                    Date measuredtime = new Date(precipMsg.getTimestamp());
                    Precipitation precipitation = new Precipitation(precipMsg.getLocationId(), precipMsg.getStationId(),
                            measuredtime, precipMsg.getLow(), precipMsg.getHigh());

                    precipitationAccessor.add(precipMsg.getLocationId(), precipMsg.getStationId(), measuredtime,
                            precipMsg.getLow(), precipMsg.getHigh());

                    LOG.info("Saved and emitting precipitation: " + jsonConverter.toJson(precipitation).replaceAll("\n", ""));
                    collector.emit(tuple, new Values(precipitation));
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
        declarer.declare(new Fields("storedobject"));
    }
}
