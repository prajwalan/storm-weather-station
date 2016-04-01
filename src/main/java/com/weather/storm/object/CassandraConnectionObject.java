package com.weather.storm.object;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;

public class CassandraConnectionObject {

    public Cluster cluster;
    public Session cassandraSession;
    public MappingManager manager;

    public void cleanup() {
        if (cassandraSession != null) {
            cassandraSession.close();
        }
        if (cluster != null) {
            cluster.close();
        }
    }
}
