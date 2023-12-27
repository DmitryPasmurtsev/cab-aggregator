package com.modsen.passengerservice.dto.request;

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
    @NotBlank(message = "{validation.passenger.name.notEmpty}")
    String name;
    @NotBlank(message = "{validation.passenger.surname.notEmpty}")
    String surname;
    @NotBlank(message = "{validation.passenger.phone.notEmpty}")
    @Pattern(regexp = "^(80(29|44|33|25)\\d{7})$", message = "{validation.passenger.phone.notValid}")
    String phone;
    @NotBlank(message = "{validation.passenger.email.notEmpty}")
    @Email(message = "{validation.passenger.email.notValid}")
    String email;
}
