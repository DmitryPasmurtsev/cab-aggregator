package com.modsen.paymentservice.dto.request;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Range;

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
    @Range(min = 100, max = 1000000,message = "Balance should be between 100 and 1000000")
    Double balance;
}
