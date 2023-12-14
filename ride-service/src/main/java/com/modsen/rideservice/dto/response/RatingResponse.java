package com.modsen.rideservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RatingResponse {
    Double rating;
    Long driverId;
}
