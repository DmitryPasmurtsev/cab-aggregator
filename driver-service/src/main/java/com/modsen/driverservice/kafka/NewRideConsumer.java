package com.modsen.driverservice.kafka;

import com.modsen.driverservice.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewRideConsumer {
    private final DriverService driverService;

    @KafkaListener(
            topics = "${topic.name.ride}",
            groupId = "${spring.kafka.consumer.group-id.ride}",
            containerFactory = "kafkaListenerContainerFactoryString"
    )
    void rideCreationListener() {
        driverService.findAvailableDriver();
    }
}
