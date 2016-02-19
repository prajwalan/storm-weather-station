package com.weather.storm.bolt;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weather.storm.cassandra.table.Precipitation;
import com.weather.storm.cassandra.table.Temperature;
import com.weather.storm.env.TopologyConstants;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

@SuppressWarnings("serial")
public class DistributionBolt extends BaseRichBolt {

    private OutputCollector collector;
    private static final Logger LOG = LoggerFactory.getLogger(DistributionBolt.class);

    @Override
    @SuppressWarnings("rawtypes")
    public void prepare(Map map, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple tuple) {
        try {
            for (Object obj : tuple.getValues()) {

                if (obj instanceof Temperature) {
                    Temperature temperature = (Temperature) obj;
                    collector.emit(TopologyConstants.STREAM_TEMPERATURE, tuple, new Values(temperature));
                }
                else if (obj instanceof Precipitation) {
                    Precipitation precipitation = (Precipitation) obj;
                    collector.emit(TopologyConstants.STREAM_PRECIPITATION, tuple, new Values(precipitation));
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
        declarer.declareStream(TopologyConstants.STREAM_TEMPERATURE, true, new Fields("object"));
        declarer.declareStream(TopologyConstants.STREAM_PRECIPITATION, true, new Fields("object"));
        declarer.declare(new Fields("object"));
    }
}
