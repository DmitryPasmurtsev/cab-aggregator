package com.modsen.driverservice.kafka;

import com.modsen.driverservice.dto.request.DriverIdDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AvailableDriverProducer {
    private final KafkaTemplate<String, DriverIdDto> kafkaTemplate;

    @Value("${topic.name.driver}")
    private String topic;

    public void notifyDriverAvailability(Long id) {
        kafkaTemplate.send(topic, new DriverIdDto(id));
    }
}
