package com.modsen.passengerservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RatingResponse {
    Double rating;
    Long passengerId;
}
