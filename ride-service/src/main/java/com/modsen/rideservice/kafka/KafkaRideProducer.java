package com.modsen.rideservice.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaRideProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    @Value("{topic.name.ride}")
    private String topic;

    public void notifyDriversAboutRideCreation() {
        kafkaTemplate.send(topic, "New ride created; Need a driver");
    }
}
