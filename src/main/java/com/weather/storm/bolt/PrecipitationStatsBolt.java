package com.weather.storm.bolt;

import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

@SuppressWarnings("serial")
public class PrecipitationStatsBolt extends BaseCassandraBolt {

    public PrecipitationStatsBolt(String host, int port, String keyspace) {
        super(host, port, keyspace);
    }

    @Override
    public void execute(Tuple tuple) {

        // -- Precipitation: Location - Year - Month - Average - Max - Min

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("temperature"));
    }
}
