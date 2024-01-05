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
    @NotNull(message = "{validation.passenger.id.notEmpty}")
    Long passengerId;
    @Min(value = 1, message = "{validation.minValue} 1")
    @Max(value = 5, message = "{validation.maxValue} 5")
    Integer ratingToDriver;
}
