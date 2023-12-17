package com.modsen.rideservice.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromoCodeUpdateRequest {
    @NotNull(message = "Coefficient should not be empty")
    @DecimalMin(value = "0.1", message = "Minimum coefficient is 0.1")
    @DecimalMax(value = "0.95", message = "Maximum coefficient is 0.95")
    Double coefficient;
}
