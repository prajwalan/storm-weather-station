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

public class WeatherTopology extends BaseTopology {

    @Override
    public TopologyBuilder getTopologyBuilder() {
        initializeKafkaZK();
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout(TopologyConstants.SPOUT_TEMPERATURE, //
                getKafkaSpout(TopologyConstants.TOPIC_TEMPERATURE, TopologyConstants.TEMPERATURE_CLIENT_ID), 1);
        builder.setBolt(TopologyConstants.BOLT_DESERIALIZE, new DeserializeBolt()) //
                .shuffleGrouping(TopologyConstants.SPOUT_TEMPERATURE);
        builder.setBolt(TopologyConstants.BOLT_STORAGE,
                new StorageBolt(EnvConstant.CASSANDRA_HOST, EnvConstant.CASSANDRA_PORT, EnvConstant.CASSANDRA_KEYSPACE)) //
                .shuffleGrouping(TopologyConstants.BOLT_DESERIALIZE);
        builder.setBolt(TopologyConstants.BOLT_TEMPERATURE_STATISTICS,
                new TemperatureStatsBolt(EnvConstant.CASSANDRA_HOST, EnvConstant.CASSANDRA_PORT, EnvConstant.CASSANDRA_KEYSPACE)) //
                .shuffleGrouping(TopologyConstants.BOLT_STORAGE);

        return builder;
    }

    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException, AuthorizationException {
        WeatherTopology topology = new WeatherTopology();
        topology.run(args, WeatherTopology.class);
    }

}
