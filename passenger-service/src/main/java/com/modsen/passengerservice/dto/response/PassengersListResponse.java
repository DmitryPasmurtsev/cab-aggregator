package com.modsen.passengerservice.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PassengersListResponse {
    Integer page;
    Integer size;
    Integer total;
    String sortedByField;
    List<PassengerResponse> passengers;

    public PassengersListResponse(List<PassengerResponse> passengers) {
        this.passengers = passengers;
        this.size = passengers.size();
        this.total = passengers.size();
    }

    public PassengersListResponse(List<PassengerResponse> passengers, String field) {
        this.passengers = passengers;
        this.size = passengers.size();
        this.total = passengers.size();
        this.sortedByField = field;
    }

    public PassengersListResponse(List<PassengerResponse> passengers, Integer page, Integer total) {
        this.passengers = passengers;
        this.size = passengers.size();
        this.total = total;
        this.page = page;
    }

    public PassengersListResponse(List<PassengerResponse> passengers, Integer page, Integer total, String field) {
        this.passengers = passengers;
        this.size = passengers.size();
        this.total = total;
        this.page = page;
        this.sortedByField = field;
    }
}
