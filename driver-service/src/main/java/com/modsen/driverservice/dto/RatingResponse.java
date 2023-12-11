package com.modsen.driverservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RatingResponse {
    Double rating;
    Long driverId;
}
