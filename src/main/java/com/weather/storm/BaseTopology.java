package com.weather.storm;

import kafka.api.OffsetRequest;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.AuthorizationException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

import com.weather.storm.env.EnvConstant;

import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;

public abstract class BaseTopology {

    private static BrokerHosts kakfaZKHosts;

    protected void initializeKafkaZK() {
        String zkConnString = EnvConstant.KAFKA_ZOOKEEPER_HOST + ":" + EnvConstant.KAFKA_ZOOKEEPER_PORT;
        kakfaZKHosts = new ZkHosts(zkConnString);
    }

    public static KafkaSpout getKafkaSpout(String topicName, String clientId) {

        SpoutConfig spoutConfig = new SpoutConfig(kakfaZKHosts, topicName, "/" + topicName, clientId);
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        spoutConfig.startOffsetTime = OffsetRequest.LatestTime();
        KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);
        return kafkaSpout;
    }

    public abstract TopologyBuilder getTopologyBuilder();

    @SuppressWarnings("rawtypes")
    public void run(String[] args, Class topology) throws AlreadyAliveException, InvalidTopologyException, AuthorizationException {
        Config conf = new Config();

        if (args != null && args.length > 0) {
            // This is executed when topology is deployed to the Storm. It expects two parameters: first is the name of the topology, second is the
            // location of configuration properties file. If the second option is not provided, it will start with bundled config.properties.

            TopologyBuilder builder = getTopologyBuilder();
            StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
        }
        else {
            // This is executed when topology is run in Eclipse - for testing/development purposes

            TopologyBuilder builder = getTopologyBuilder();
            LocalCluster cluster = new LocalCluster();
            conf.setDebug(true);
            cluster.submitTopology("test", conf, builder.createTopology());
            Utils.sleep(1000000);
            cluster.killTopology("test");
            cluster.shutdown();
        }
    }
}