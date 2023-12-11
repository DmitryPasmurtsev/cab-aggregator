package com.modsen.passengerservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PassengerCreationRequest {
    @NotBlank(message = "Name should not be empty")
    String name;
    @NotBlank(message = "Surname should not be empty")
    String surname;
    @NotBlank(message = "Phone should not be empty")
    @Pattern(regexp = "^(80(29|44|33|25)\\d{7})$", message = "Phone pattern is 80xxxxxxxxx. Length is 11 characters. Valid codes are 29, 44, 33, 25")
    String phone;
    @NotBlank(message = "Email should not be empty")
    @Email(message = "Email address not valid")
    String email;
}
