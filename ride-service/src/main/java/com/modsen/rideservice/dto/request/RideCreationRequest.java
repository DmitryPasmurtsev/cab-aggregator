package com.modsen.rideservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RideCreationRequest {
    @NotBlank(message = "Passenger id should not be empty")
    Long passengerId;
    @NotBlank(message = "Pick-up address should not be empty")
    String pickUp;
    @NotBlank(message = "Destination address should not be empty")
    String destination;
}
