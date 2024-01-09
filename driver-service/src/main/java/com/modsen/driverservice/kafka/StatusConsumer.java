package com.modsen.driverservice.kafka;

import com.modsen.driverservice.dto.request.DriverIdDto;
import com.modsen.driverservice.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

@RequiredArgsConstructor
public class StatusConsumer {
    private final DriverService driverService;

    @KafkaListener(
            topics = "${topic.name.status}",
            groupId = "${spring.kafka.consumer.group-id.status}",
            containerFactory = "kafkaListenerContainerFactoryDriver"
    )
    void changeDriverStatusListener(@Payload DriverIdDto dto) {
        driverService.changeAvailabilityStatus(dto.getDriverId());
    }
}
