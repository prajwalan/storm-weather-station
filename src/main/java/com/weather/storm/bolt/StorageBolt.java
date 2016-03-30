package com.weather.storm.bolt;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.weather.storm.cassandra.accessor.TemperatureAccessor;
import com.weather.storm.cassandra.table.Temperature;
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

    private Gson jsonConverter;

    public StorageBolt(String host, int port, String keyspace) {
        super(host, port, keyspace);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        super.prepare(stormConf, context, collector);

        initAccessors();
        jsonConverter = CommonUtil.createJsonConvertor();
    }

    private void initAccessors() {
        if (manager != null) {
            temperatureAccessor = manager.createAccessor(TemperatureAccessor.class);
        }
    }

    @Override
    public void execute(Tuple tuple) {

        try {
            for (Object msg : tuple.getValues()) {

                if (msg instanceof TemperatureMsg) {
                    TemperatureMsg tempMsg = (TemperatureMsg) msg;
                    storeInDatabase(tempMsg);

                    Temperature temperature = new Temperature(tempMsg.getLocationId(), tempMsg.getStationId(),
                            new Date(tempMsg.getTimestamp()), tempMsg.getMeasurement());
                    LOG.info("Saved and emitting temperature: " + jsonConverter.toJson(temperature).replaceAll("\n", ""));
                    collector.emit(tuple, new Values(temperature));
                }

            }
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }

        collector.ack(tuple);
    }

    public void storeInDatabase(TemperatureMsg tempMsg) {
        if (temperatureAccessor != null) {
            temperatureAccessor.add(tempMsg.getLocationId(), tempMsg.getStationId(), new Date(tempMsg.getTimestamp()),
                    tempMsg.getMeasurement());
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("storedobject"));
    }
}
