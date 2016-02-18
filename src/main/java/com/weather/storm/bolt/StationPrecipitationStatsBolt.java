package com.weather.storm.bolt;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

@SuppressWarnings("serial")
public class StationPrecipitationStatsBolt extends BaseRichBolt {

    private OutputCollector collector;

    public void execute(Tuple tuple) {

        // -- Precipitation: Station - Year - Month - Average - Max - Min
        // -- Precipitation: Station - Year - Week - Average - Max - Min

    }

    @SuppressWarnings("rawtypes")
    public void prepare(Map map, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("temperature"));
    }
}