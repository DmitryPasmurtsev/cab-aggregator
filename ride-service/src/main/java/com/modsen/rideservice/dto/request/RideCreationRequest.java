package com.modsen.rideservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RideCreationRequest {
    @NotNull(message = "Passenger id should not be empty")
    Long passengerId;
    @NotBlank(message = "Pick-up address should not be empty")
    String pickUp;
    @NotBlank(message = "Destination address should not be empty")
    String destination;
}
