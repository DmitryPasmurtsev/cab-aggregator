package com.modsen.rideservice.kafka;

import com.modsen.rideservice.dto.request.RatingUpdateDto;
import com.modsen.rideservice.entity.Rating;
import com.modsen.rideservice.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
@RequiredArgsConstructor
public class KafkaRatingProducer {
    @Value("${topic.name.passenger-rating}")
    private String passengerTopic;
    @Value("${topic.name.driver-rating}")
    private String driverTopic;

    private final RatingRepository ratingRepository;
    private final KafkaTemplate<String, RatingUpdateDto> kafkaTemplateRatingUpdateDto;


    public void updateRatingForPassenger(Long id) {
        List<Rating> ratings = ratingRepository.findAllByRidePassengerIdAndPassengerRatingIsNotNull(id);
        List<Integer> passengerRatings = ratings.stream()
                .map(Rating::getPassengerRating)
                .toList();
        Double rating = calculateRating(passengerRatings);
        kafkaTemplateRatingUpdateDto.send(passengerTopic, new RatingUpdateDto(id, rating));
    }

    public void updateRatingForDriver(Long id) {
        List<Rating> ratings = ratingRepository.findAllByRideDriverIdAndDriverRatingIsNotNull(id);
        List<Integer> driverRatings = ratings.stream()
                .map(Rating::getDriverRating)
                .toList();
        Double rating = calculateRating(driverRatings);
        kafkaTemplateRatingUpdateDto.send(driverTopic, new RatingUpdateDto(id, rating));
    }

    private Double calculateRating(List<Integer> ratings) {
        AtomicReference<Integer> totalRating = new AtomicReference<>(0);
        ratings.forEach(r -> totalRating.updateAndGet(v -> v + r));
        return Math.round(totalRating.get() * 100.0 / ratings.size()) / 100.0;
    }

}
