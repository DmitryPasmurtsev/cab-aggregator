package com.modsen.rideservice.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverFinishRequest {
    @NotBlank(message = "Driver's id should not be empty")
    Long driverId;
    @NotBlank(message = "Ride id should not be empty")
    @Min(value = 1, message = "Minimum rating = 1")
    @Max(value = 5, message = "Maximum rating = 5")
    int ratingToPassenger;
}
