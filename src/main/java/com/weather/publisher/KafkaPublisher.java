package com.weather.publisher;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Publishes message to Kafka broker
 *
 */
public class KafkaPublisher {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaPublisher.class);

    private Properties props;
    private KafkaProducer<String, String> kafkaProducer;

    public KafkaPublisher(String brokerList) {
        props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.ACKS_CONFIG, "0");

        // TODO: Read these from properties file
        props.put(ProducerConfig.METADATA_FETCH_TIMEOUT_CONFIG, 1000);
        props.put(ProducerConfig.TIMEOUT_CONFIG, 1000);

        kafkaProducer = new KafkaProducer<String, String>(props);
    }

    /**
     * Publishes the given string message on the given topic to the Kafka broker.
     * 
     * @param topic
     * @param message
     * @throws KafkaPublishException
     * @throws InterruptedException,
     *             ExecutionException, TimeoutException
     * @throws MqttException
     */
    public void publish(String topic, String message) throws KafkaPublishException {

        LOG.info("Publising: " + message);

        try {
            ProducerRecord<String, String> data = new ProducerRecord<String, String>(topic, message);
            // Execute blocking connection to Kafka broker with 1 second timeout
            kafkaProducer.send(data).get(1, TimeUnit.SECONDS);
        }
        catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOG.info("Got a Kafka timeout. Retrying once...");
            // Recreate the producer
            kafkaProducer = new KafkaProducer<String, String>(props);

            // Try once: One time failover
            try {
                ProducerRecord<String, String> data = new ProducerRecord<String, String>(topic, message);
                // Execute blocking connection to Kafka broker with 1 second timeout
                kafkaProducer.send(data).get(1, TimeUnit.SECONDS);
            }
            catch (InterruptedException | ExecutionException | TimeoutException e1) {
                LOG.error(e1.getMessage());
                LOG.info("Got a Kafka timeout upon retry also. Giving up...");

                // Throw an exception to notify the caller
                throw new KafkaPublishException("Unable to pulish to Kafka even after one try. Got error: " + e1.getMessage());
            }

        }
    }

    public void closePublisher() {
        kafkaProducer.close();
    }
}