package com.weather.storm.env;

public class EnvConstant {

    public static String KAFKA_ZOOKEEPER_HOST = "192.168.232.162";
    public static int KAFKA_ZOOKEEPER_PORT = 2182;
    public static String KAFKA_BROKER_LIST = "192.168.232.162:9092";

    public static String CASSANDRA_HOST = "192.168.232.162";
    public static int CASSANDRA_PORT = 9042;
    public static final String CASSANDRA_KEYSPACE = "weather_station_keyspace";
}
