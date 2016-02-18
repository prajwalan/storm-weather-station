package com.weather.storm.bolt;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.google.gson.Gson;
import com.weather.storm.env.TopologyConstants;
import com.weather.storm.object.Precipitation;
import com.weather.storm.object.Temperature;
import com.weather.storm.util.CommonUtil;

@SuppressWarnings("serial")
public class DeserializeBolt extends BaseRichBolt {

    private static final Logger LOG = LoggerFactory.getLogger(DeserializeBolt.class);
    private OutputCollector collector;
    private Gson jsonConverter;

    public void execute(Tuple tuple) {
        LOG.info("Received : " + tuple.getValue(0).toString());

        String source = tuple.getSourceComponent();
        try {
            for (Object msg : tuple.getValues()) {
                String message = msg.toString();
                if (source.equalsIgnoreCase(TopologyConstants.SPOUT_TEMPERATURE)) {
                    Temperature temp = jsonConverter.fromJson(message, Temperature.class);
                    LOG.info("Deserialized to Temperature: " + temp.toString().replaceAll("\\n", ""));
                    collector.emit(tuple, new Values(temp));
                }
                else if (source.equalsIgnoreCase(TopologyConstants.SPOUT_PRECIPITATION)) {
                    Precipitation precip = jsonConverter.fromJson(message, Precipitation.class);
                    LOG.info("Deserialized to Precipitation: " + precip.toString().replaceAll("\\n", ""));
                    collector.emit(tuple, new Values(precip));
                }

            }
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }

        collector.ack(tuple);
    }

    @SuppressWarnings("rawtypes")
    public void prepare(Map map, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        jsonConverter = CommonUtil.createJsonConvertor();
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("temperature"));
    }
}
