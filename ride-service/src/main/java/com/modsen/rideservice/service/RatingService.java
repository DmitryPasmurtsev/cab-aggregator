package com.modsen.rideservice.service;

import com.modsen.rideservice.dto.response.RatingResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public interface RatingService {

    RatingResponse getRatingForPassenger(Long id);

    RatingResponse getRatingForDriver(Long id);
}
