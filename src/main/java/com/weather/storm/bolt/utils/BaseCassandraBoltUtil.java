package com.weather.storm.bolt.utils;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.mapping.MappingManager;
import com.weather.storm.object.CassandraConnectionObject;

public class BaseCassandraBoltUtil {

    public static CassandraConnectionObject createConnectionObject(String host, int port, String keyspace) {
        CassandraConnectionObject cassandraConnObjects = new CassandraConnectionObject();

        QueryOptions options = new QueryOptions();
        options.setFetchSize(5000);

        cassandraConnObjects.cluster = Cluster.builder().addContactPoint(host).withPort(port).withQueryOptions(options).build();
        cassandraConnObjects.cassandraSession = cassandraConnObjects.cluster.connect(keyspace);
        cassandraConnObjects.manager = new MappingManager(cassandraConnObjects.cassandraSession);

        return cassandraConnObjects;
    }

}
