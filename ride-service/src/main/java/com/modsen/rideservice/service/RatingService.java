package com.modsen.rideservice.service;

import com.modsen.rideservice.dto.response.RatingResponse;

public interface RatingService {

    RatingResponse getRatingForPassenger(Long id);

    RatingResponse getRatingForDriver(Long id);
}
