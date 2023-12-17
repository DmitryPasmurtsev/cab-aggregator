package com.modsen.rideservice.dto.response;

import com.modsen.rideservice.enums.Status;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RideResponse {
    Long id;
    String pickUp;
    String destination;
    Date date;
    Status status;
    Double initialCost;
    Double finalCost;
    Long passengerId;
    Long driverId;
}

