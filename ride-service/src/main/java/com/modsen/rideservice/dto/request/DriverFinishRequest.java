package com.modsen.rideservice.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverFinishRequest {
    @NotNull(message = "{validation.driver.id.notEmpty}")
    Long driverId;
    @NotNull(message = "{validation.rating.notEmpty}")
    @Min(value = 1, message = "{validation.minValue} 1")
    @Max(value = 5, message = "{validation.maxValue} 5")
    Integer ratingToPassenger;
}
