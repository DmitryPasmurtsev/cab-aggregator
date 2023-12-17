package com.modsen.rideservice.service.impl;

import com.modsen.rideservice.dto.response.RatingResponse;
import com.modsen.rideservice.entity.Rating;
import com.modsen.rideservice.repository.RatingRepository;
import com.modsen.rideservice.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;

    public RatingResponse getRatingForPassenger(Long id) {
        List<Rating> ratings = ratingRepository.findAllByRidePassengerIdAndPassengerRatingIsNotNull(id);
        if(ratings.isEmpty()) return new RatingResponse(id, null);
        List<Integer> passengerRatings = ratings.stream()
                .map(Rating::getPassengerRating)
                .toList();
        return new RatingResponse(id, calculateRating(passengerRatings));
    }

    public RatingResponse getRatingForDriver(Long id) {
        List<Rating> ratings = ratingRepository.findAllByRideDriverIdAndDriverRatingIsNotNull(id);
        if(ratings.isEmpty()) return new RatingResponse(id, null);
        List<Integer> driverRatings = ratings.stream()
                .map(Rating::getDriverRating)
                .toList();
        return new RatingResponse(id, calculateRating(driverRatings));
    }

    private Double calculateRating(List<Integer> ratings) {
        AtomicReference<Integer> totalRating = new AtomicReference<>(0);
        ratings.forEach(r -> totalRating.updateAndGet(v -> v + r));
        return Math.round(totalRating.get()*100.0/ratings.size())/100.0;
    }
}
