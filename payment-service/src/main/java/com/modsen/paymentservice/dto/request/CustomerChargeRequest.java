package com.modsen.paymentservice.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerChargeRequest {
    @NotNull(message = "Amount is mandatory")
    @DecimalMin(value = "0.01", message = "Charge minimum is 0.01")
    @DecimalMax(value = "5000", message = "Charge maximum is 5000")
    Double amount;
    @NotNull(message = "Passenger is mandatory")
    @Min(value = 1, message = "Id min value is 1")
    long passengerId;
}
