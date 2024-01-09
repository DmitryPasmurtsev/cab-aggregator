package com.modsen.driverservice.kafka;

import com.modsen.driverservice.dto.request.RatingUpdateDto;
import com.modsen.driverservice.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RatingConsumer {
    private final DriverService driverService;

    @KafkaListener(
            topics = "${topic.name.rating}",
            groupId = "${spring.kafka.consumer.group-id.rating}",
            containerFactory = "kafkaListenerContainerFactoryRating"
    )
    void ratingUpdateListener(@Payload RatingUpdateDto dto) {
        driverService.updateRating(dto);
    }
}
