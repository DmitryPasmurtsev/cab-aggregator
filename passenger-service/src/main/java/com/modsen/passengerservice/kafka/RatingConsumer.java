package com.modsen.passengerservice.kafka;

import com.modsen.passengerservice.dto.request.RatingUpdateDto;
import com.modsen.passengerservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RatingConsumer {
    private final PassengerService passengerService;

    @KafkaListener(
            topics = "${topic.name.rating}",
            groupId = "${spring.kafka.consumer.group-id.rating}",
            containerFactory = "kafkaListenerContainerFactoryRating"
    )
    void ratingUpdateListener(@Payload RatingUpdateDto dto) {
        passengerService.updateRating(dto);
    }
}
