package com.modsen.driverservice.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriversListResponse {
    int size;
    List<DriverResponse> drivers;

    public DriversListResponse(List<DriverResponse> passengers) {
        this.drivers = passengers;
        this.size = passengers.size();
    }
}
