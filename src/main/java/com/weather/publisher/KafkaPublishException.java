package com.weather.publisher;

@SuppressWarnings("serial")
public class KafkaPublishException extends Exception {

    public KafkaPublishException(String message) {
        super(message);
    }

}
