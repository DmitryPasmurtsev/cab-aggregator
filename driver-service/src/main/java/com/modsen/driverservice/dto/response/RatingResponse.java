package com.modsen.driverservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RatingResponse {
    Double rating;
    Long driverId;
}
