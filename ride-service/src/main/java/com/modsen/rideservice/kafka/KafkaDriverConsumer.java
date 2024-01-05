package com.modsen.rideservice.kafka;

import com.modsen.rideservice.dto.request.DriverIdDto;
import com.modsen.rideservice.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaDriverConsumer {
    private final RideService rideService;

    @KafkaListener(
            topics = "${topic.name.driver}",
            groupId = "${spring.kafka.consumer.group-id.driver}",
            containerFactory = "kafkaListenerContainerFactoryDriver"
    )
    void availableDriverListener(@Payload DriverIdDto dto) {
        System.err.println("Свободный водитель: " + dto.getDriverId());
        rideService.setDriverToRide(dto.getDriverId());
    }
}
