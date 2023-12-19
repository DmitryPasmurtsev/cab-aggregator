package com.modsen.rideservice.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PassengerFinishRequest {
    @NotNull(message = "Passenger's id should not be empty")
    Long passengerId;
    @Min(value = 1, message = "Minimum rating = 1")
    @Max(value = 5, message = "Maximum rating = 5")
    Integer ratingToDriver;
}
