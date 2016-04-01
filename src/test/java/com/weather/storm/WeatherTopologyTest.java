package com.weather.storm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.datastax.driver.mapping.MappingManager;
import com.google.gson.Gson;
import com.weather.storm.bolt.DeserializeBolt;
import com.weather.storm.bolt.StorageBolt;
import com.weather.storm.bolt.TemperatureStatsBolt;
import com.weather.storm.bolt.utils.AccessorUtil;
import com.weather.storm.bolt.utils.BaseCassandraBoltUtil;
import com.weather.storm.bolt.utils.StorageBoltUtil;
import com.weather.storm.bolt.utils.TemperatureStatsBoltUtil;
import com.weather.storm.cassandra.accessor.MonthlyStatAccessor;
import com.weather.storm.cassandra.accessor.TemperatureAccessor;
import com.weather.storm.cassandra.table.MonthlyStat;
import com.weather.storm.cassandra.table.Temperature;
import com.weather.storm.env.TopologyConstants;
import com.weather.storm.object.CassandraConnectionObject;
import com.weather.storm.object.TemperatureMsg;

import backtype.storm.Config;
import backtype.storm.ILocalCluster;
import backtype.storm.Testing;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.testing.CompleteTopologyParam;
import backtype.storm.testing.FeederSpout;
import backtype.storm.testing.FixedTuple;
import backtype.storm.testing.MkClusterParam;
import backtype.storm.testing.MockedSources;
import backtype.storm.testing.TestJob;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({ BaseCassandraBoltUtil.class, AccessorUtil.class, StorageBoltUtil.class, TemperatureStatsBoltUtil.class,
        StorageBolt.class, TemperatureStatsBolt.class })
public class WeatherTopologyTest {

    @Before
    public void setUp() throws Exception {

        PowerMock.mockStatic(BaseCassandraBoltUtil.class);
        EasyMock.expect(BaseCassandraBoltUtil.createConnectionObject("", 0, "")).andReturn(new CassandraConnectionObject())
                .anyTimes();

        PowerMock.mockStatic(AccessorUtil.class);
        EasyMock.expect(AccessorUtil.createMonthlyStatAccessor(EasyMock.anyObject(MappingManager.class))).andReturn(null)
                .anyTimes();
        EasyMock.expect(AccessorUtil.createTemperatureAccessor(EasyMock.anyObject(MappingManager.class))).andReturn(null)
                .anyTimes();

        PowerMock.mockStatic(StorageBoltUtil.class);
        EasyMock.expect(StorageBoltUtil.storeInDatabase(EasyMock.anyObject(TemperatureAccessor.class),
                EasyMock.anyObject(TemperatureMsg.class))).andReturn(null).anyTimes();

        PowerMock.mockStatic(TemperatureStatsBoltUtil.class);
        EasyMock.expect(TemperatureStatsBoltUtil.getFromDatabase(EasyMock.anyObject(MonthlyStatAccessor.class), EasyMock.anyInt(),
                EasyMock.anyInt(), EasyMock.anyInt())).andReturn(null).anyTimes();
        EasyMock.expect(TemperatureStatsBoltUtil.storeInDatabase(EasyMock.anyObject(MonthlyStatAccessor.class),
                EasyMock.anyObject(Temperature.class), EasyMock.anyObject(MonthlyStat.class))).andReturn(null).anyTimes();

        PowerMock.replayAll();

    }

    private static String getTemperatureValue(int locationid, int stationid, String value) {
        TemperatureMsg measurementMsg = new TemperatureMsg(locationid, stationid, System.currentTimeMillis(), value);
        return new Gson().toJson(measurementMsg);
    }

    @Test
    public void testWeatherTopology() {

        MkClusterParam mkClusterParam = new MkClusterParam();
        mkClusterParam.setSupervisors(1);
        Config daemonConf = new Config();
        daemonConf.put(Config.STORM_LOCAL_MODE_ZMQ, false);
        mkClusterParam.setDaemonConf(daemonConf);

        Testing.withSimulatedTimeLocalCluster(mkClusterParam, new TestJob() {
            @Override
            public void run(ILocalCluster cluster) throws AlreadyAliveException, InvalidTopologyException, InterruptedException {

                FeederSpout feederSpout = new FeederSpout(new Fields("measurements"));

                // Build the test topology
                DeserializeBolt deserializeBolt = new DeserializeBolt();
                StorageBolt storageBolt = new StorageBolt("", 0, "");
                TemperatureStatsBolt tempStatBolt = new TemperatureStatsBolt("", 0, "");
                TopologyBuilder builder = WeatherTopology.buildTopology(feederSpout, deserializeBolt, storageBolt, tempStatBolt);

                StormTopology topology = builder.createTopology();

                // prepare the config
                Config conf = new Config();
                conf.setNumWorkers(6);

                // prepare the mock data
                MockedSources mockedSources = new MockedSources();
                mockedSources.addMockData(TopologyConstants.SPOUT_TEMPERATURE, //
                        new Values(getTemperatureValue(1, 21, "4.6")), //
                        new Values(getTemperatureValue(1, 22, "6.64")), //
                        new Values(getTemperatureValue(2, 31, "7.85")), //
                        new Values(getTemperatureValue(2, 32, "9.04")), //
                        new Values(getTemperatureValue(1, 21, "5.98")), //
                        new Values(getTemperatureValue(1, 22, "6.12")), //
                        new Values(getTemperatureValue(2, 31, "8.81")), //
                        new Values(getTemperatureValue(2, 32, "9.34")) //
                );

                // -- Expected:
                // -- Location: 1 -> Avg: 5.84, Max: 6.64, Min: 4.6
                // -- Location: 2 -> Avg: 8.76, Max: 9.34, Min: 7.85

                CompleteTopologyParam completeTopologyParam = new CompleteTopologyParam();
                completeTopologyParam.setMockedSources(mockedSources);
                completeTopologyParam.setStormConf(conf);

                // Carray out mock execution of the topology
                Map<?, ?> result = Testing.completeTopology(cluster, topology, completeTopologyParam);
                List<?> values = (List<?>) result.get(TopologyConstants.BOLT_TEMPERATURE_STATISTICS);

                // Ensure that we got the test emits
                Assert.assertTrue(values != null && values.size() > 0);

                HashMap<Integer, MonthlyStat> finalStats = new HashMap<Integer, MonthlyStat>();
                for (Object obj : values) {
                    MonthlyStat stat = (MonthlyStat) ((FixedTuple) obj).values.get(0);
                    finalStats.put(stat.getLocationid(), stat);
                }

                Assert.assertEquals("5.84", finalStats.get(1).getAverage());
                Assert.assertEquals("6.64", finalStats.get(1).getMax());
                Assert.assertEquals("4.6", finalStats.get(1).getMin());
                Assert.assertEquals("8.76", finalStats.get(2).getAverage());
                Assert.assertEquals("9.34", finalStats.get(2).getMax());
                Assert.assertEquals("7.85", finalStats.get(2).getMin());
            }
        });

    }

}
