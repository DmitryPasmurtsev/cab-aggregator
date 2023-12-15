package com.modsen.rideservice.service.impl;

import com.modsen.rideservice.dto.request.*;
import com.modsen.rideservice.dto.response.RatingResponse;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.entity.Rating;
import com.modsen.rideservice.entity.Ride;
import com.modsen.rideservice.repository.RatingRepository;
import com.modsen.rideservice.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;
    private final ModelMapper modelMapper;

    private RideResponse toDTO(Ride ride) {
        if (ride == null) return null;
        return modelMapper.map(ride, RideResponse.class);
    }

    private Ride toModel(RideCreationRequest dto) {
        return modelMapper.map(dto, Ride.class);
    }

    public RatingResponse getRatingForPassenger(Long id) {
        List<Rating> ratings = ratingRepository.findAllByPassengerIdAndPassengerRatingIsNotNull(id);
        return new RatingResponse(id, calculateRating(ratings));
    }

    public RatingResponse getRatingForDriver(Long id) {
        List<Rating> ratings = ratingRepository.findAllByDriverIdAndDriverRatingIsNotNull(id);
        return new RatingResponse(id, calculateRating(ratings));
    }

    private Double calculateRating(List<Rating> ratings) {
        if(ratings.isEmpty()) return null;
        AtomicReference<Integer> totalRating = new AtomicReference<>(0);
        ratings.forEach(r -> totalRating.updateAndGet(v -> v + r.getPassengerRating()));
        return Math.round(totalRating.get()*100.0/ratings.size())/100.0;
    }
}
