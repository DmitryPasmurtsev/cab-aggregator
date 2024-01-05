package com.modsen.rideservice.kafka;

import com.modsen.rideservice.dto.request.DriverIdDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaDriverStatusProducer {
    private final KafkaTemplate<String, DriverIdDto> kafkaTemplate;

    @Value("${topic.name.status}")
    private String topic;

    public void changeDriverAvailabilityStatus(Long driverId) {
        kafkaTemplate.send(topic, new DriverIdDto(driverId));
    }

}
