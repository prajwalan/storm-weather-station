package com.weather.storm.bolt;

import java.util.Map;

import com.weather.storm.bolt.utils.BaseCassandraBoltUtil;
import com.weather.storm.object.CassandraConnectionObject;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.base.BaseRichBolt;

@SuppressWarnings("serial")
public abstract class BaseCassandraBolt extends BaseRichBolt {

    protected OutputCollector collector;

    protected CassandraConnectionObject cassandraConnObject;

    private String host;
    private int port;
    private String keyspace;

    public BaseCassandraBolt(String host, int port, String keyspace) {
        this.host = host;
        this.port = port;
        this.keyspace = keyspace;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        cassandraConnObject = new CassandraConnectionObject();

        cassandraConnObject = BaseCassandraBoltUtil.createConnectionObject(host, port, keyspace);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        cassandraConnObject.cleanup();
    }
}