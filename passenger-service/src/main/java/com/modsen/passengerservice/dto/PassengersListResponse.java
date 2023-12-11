package com.modsen.passengerservice.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PassengersListResponse {
    int size;
    List<PassengerResponse> passengers;

    public PassengersListResponse(List<PassengerResponse> passengers) {
        this.passengers = passengers;
        this.size = passengers.size();
    }
}
