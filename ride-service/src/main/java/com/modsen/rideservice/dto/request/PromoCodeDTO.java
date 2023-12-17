package com.modsen.rideservice.dto.request;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromoCodeDTO {
    @NotBlank(message = "Name should not be empty")
    String name;
    @NotNull(message = "Coefficient should not be empty")
    @DecimalMin(value = "0.1", message = "Minimum coefficient is 0.1")
    @DecimalMax(value = "0.95", message = "Maximum coefficient is 0.95")
    Double coefficient;
}
