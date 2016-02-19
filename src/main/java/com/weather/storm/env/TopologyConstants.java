package com.weather.storm.env;

public class TopologyConstants {

    public static final String SPOUT_TEMPERATURE = "spout_temperature";
    public static final String TOPIC_TEMPERATURE = "temperatureseries";
    public static final String TEMPERATURE_CLIENT_ID = "temperature-client-1";

    public static final String SPOUT_PRECIPITATION = "spout_precipitation";
    public static final String TOPIC_PRECIPITATION = "precipitationseries";
    public static final String PRECIPITATION_CLIENT_ID = "precipitation-client-1";

    public static final String BOLT_DESERIALIZE = "bolt_deserialize";
    public static final String BOLT_STORAGE = "bolt_storage";
    public static final String BOLT_DISTRIBUTION = "bolt_distribution";
    public static final String BOLT_TEMPERATURE_STATISTICS = "bolt_temperature_stats";
    public static final String BOLT_PRECIPITATION_STATISTICS = "bolt_precipitation_stats";

    public static final String STREAM_TEMPERATURE = "stream_temperature";
    public static final String STREAM_PRECIPITATION = "stream_precipitation";

}
