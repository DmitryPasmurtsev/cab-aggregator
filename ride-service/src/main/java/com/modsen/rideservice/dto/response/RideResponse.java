package com.modsen.rideservice.dto.response;

import com.modsen.rideservice.enums.Status;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RideResponse {
    Long id;
    String pickUp;
    String destination;
    LocalDate date;
    Status status;
    Double cost;
    Long passengerId;
    Long driverId;
}

