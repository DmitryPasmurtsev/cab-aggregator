package com.modsen.passengerservice.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class PassengersListResponse {

    int size;
    List<PassengerResponse> passengers;

    public PassengersListResponse(List<PassengerResponse> passengers) {
        this.passengers = passengers;
        this.size = passengers.size();
    }
}
