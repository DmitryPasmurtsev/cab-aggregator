package com.modsen.rideservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RideCreationRequest {
    @NotNull(message = "{validation.passenger.id.notEmpty}")
    Long passengerId;
    @NotBlank(message = "{validation.rides.pick-up.notEmpty}")
    String pickUp;
    @NotBlank(message = "{validation.rides.destination.notEmpty}")
    String destination;
    String promoCode;
    @NotBlank(message = "{validation.paymentMethod.notEmpty}")
    @Pattern(regexp = "^(CASH|NO_CASH)$", message = "{validation.paymentMethod.notValid}")
    String paymentMethod;
}
