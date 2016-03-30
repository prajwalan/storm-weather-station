package com.weather.storm.bolt;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.base.BaseRichBolt;

@SuppressWarnings("serial")
public abstract class BaseCassandraBolt extends BaseRichBolt {

    protected OutputCollector collector;

    private Cluster cluster;
    private Session cassandraSession;
    protected MappingManager manager;

    private String host;
    private int port;
    private String keyspace;

    public BaseCassandraBolt(String host, int port, String keyspace) {
        this.host = host;
        this.port = port;
        this.keyspace = keyspace;
    }

    protected void initialize() {
        QueryOptions options = new QueryOptions();
        options.setFetchSize(5000);

        if (!StringUtils.isEmpty(host)) {
            cluster = Cluster.builder().addContactPoint(host).withPort(port).withQueryOptions(options).build();
            cassandraSession = cluster.connect(keyspace);

            manager = new MappingManager(cassandraSession);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        initialize();
    }

    @Override
    public void cleanup() {

        if (cassandraSession != null) {
            cassandraSession.close();
        }
        if (cluster != null) {
            cluster.close();
        }
        super.cleanup();
    }
}