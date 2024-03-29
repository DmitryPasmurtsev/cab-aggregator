package com.modsen.paymentservice.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerRequest {
    @NotBlank(message = "Name can't be empty")
    String name;
    @Email(message = "Email is not valid")
    @NotBlank(message = "Email can't be empty")
    String email;
    @Pattern(regexp = "^(80(29|44|33|25)\\d{7})$")
    @NotBlank(message = "Phone can't be empty")
    String phone;
    @NotNull(message = "Passenger can't be empty")
    @Min(value = 1, message = "Min value is 1")
    long passengerId;
    @NotNull(message = "Balance can't be empty")
    @DecimalMin(value = "0.01", message = "Balance minimum is 0.01")
    @DecimalMax(value = "5000", message = "Balance maximum is 5000")
    Double balance;
}
