package com.weather.publisher;

import org.apache.commons.lang.math.RandomUtils;

import com.google.gson.Gson;
import com.weather.storm.env.EnvConstant;
import com.weather.storm.env.TopologyConstants;
import com.weather.storm.object.PrecipitationMsg;
import com.weather.storm.object.TemperatureMsg;
import com.weather.storm.util.CommonUtil;

public class TupleGenerator {

    public static void main(String[] args) throws KafkaPublishException {

        // -- Generate random test data and publish to Kafka
        int numLocation = 100;
        int numStationPerLocation = 4;

        KafkaPublisher publisher = new KafkaPublisher(EnvConstant.KAFKA_BROKER_LIST);
        Gson jsonConverter = CommonUtil.createJsonConvertor();

        int n = 1000;
        while (n > 0) {
            int randomLocation = RandomUtils.nextInt(numLocation) + 1;
            int randomStation = Integer.parseInt("" + randomLocation + (RandomUtils.nextInt(numStationPerLocation) + 1));

            float tempMeasurement = CommonUtil.round(RandomUtils.nextFloat() * 20.0f, 2);
            float precipLow = CommonUtil.round(RandomUtils.nextFloat() * 2.0f, 2);
            float precipHigh = CommonUtil.round(precipLow + RandomUtils.nextFloat() * 4.0f, 2);

            TemperatureMsg tempMsg = new TemperatureMsg(randomLocation, randomStation, System.currentTimeMillis(),
                    tempMeasurement);
            PrecipitationMsg precipMsg = new PrecipitationMsg(randomLocation, randomStation, System.currentTimeMillis(),
                    precipLow, precipHigh);

            publisher.publish(TopologyConstants.TOPIC_TEMPERATURE, jsonConverter.toJson(tempMsg));
            publisher.publish(TopologyConstants.TOPIC_PRECIPITATION, jsonConverter.toJson(precipMsg));

            n--;
        }

    }

}
