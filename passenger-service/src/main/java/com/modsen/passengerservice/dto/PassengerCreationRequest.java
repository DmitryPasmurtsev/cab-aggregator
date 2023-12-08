package com.modsen.passengerservice.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PassengerCreationRequest{
    String name;
    String surname;
    String phone;
    String email;
}
