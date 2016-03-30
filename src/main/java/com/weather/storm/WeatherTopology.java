package com.weather.storm;

import com.weather.storm.bolt.DeserializeBolt;
import com.weather.storm.bolt.TemperatureStatsBolt;
import com.weather.storm.bolt.StorageBolt;
import com.weather.storm.env.EnvConstant;
import com.weather.storm.env.TopologyConstants;

import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.AuthorizationException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseRichSpout;

public class WeatherTopology extends BaseTopology {

    public static TopologyBuilder buildTopology(BaseRichSpout spout, DeserializeBolt deserializeBolt, StorageBolt storageBolt,
            TemperatureStatsBolt tempStatsBolt) {

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout(TopologyConstants.SPOUT_TEMPERATURE, spout, 1);
        builder.setBolt(TopologyConstants.BOLT_DESERIALIZE, deserializeBolt) //
                .shuffleGrouping(TopologyConstants.SPOUT_TEMPERATURE);
        builder.setBolt(TopologyConstants.BOLT_STORAGE, storageBolt) //
                .shuffleGrouping(TopologyConstants.BOLT_DESERIALIZE);
        builder.setBolt(TopologyConstants.BOLT_TEMPERATURE_STATISTICS, tempStatsBolt) //
                .shuffleGrouping(TopologyConstants.BOLT_STORAGE);

        return builder;
    }

    @Override
    public TopologyBuilder getTopologyBuilder() {
        initializeKafkaZK();

        DeserializeBolt deserializeBolt = new DeserializeBolt();
        StorageBolt storageBolt = new StorageBolt(EnvConstant.CASSANDRA_HOST, EnvConstant.CASSANDRA_PORT,
                EnvConstant.CASSANDRA_KEYSPACE);
        TemperatureStatsBolt tempStatsBolt = new TemperatureStatsBolt(EnvConstant.CASSANDRA_HOST, EnvConstant.CASSANDRA_PORT,
                EnvConstant.CASSANDRA_KEYSPACE);

        return WeatherTopology.buildTopology(
                getKafkaSpout(TopologyConstants.TOPIC_TEMPERATURE, TopologyConstants.TEMPERATURE_CLIENT_ID),
                deserializeBolt, storageBolt, tempStatsBolt);
    }

    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException, AuthorizationException {
        WeatherTopology topology = new WeatherTopology();
        topology.run(args, WeatherTopology.class);
    }

}
