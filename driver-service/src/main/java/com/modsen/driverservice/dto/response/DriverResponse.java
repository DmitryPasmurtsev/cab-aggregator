package com.modsen.driverservice.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverResponse {
    Long id;
    String name;
    String surname;
    String phone;
    boolean isAvailable;
    Double rating;
}
