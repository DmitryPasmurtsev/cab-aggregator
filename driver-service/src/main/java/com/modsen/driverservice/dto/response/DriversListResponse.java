package com.modsen.driverservice.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriversListResponse {
    Integer page;
    Integer size;
    Integer total;
    String sortedByField;
    List<DriverResponse> drivers;

    public DriversListResponse(List<DriverResponse> passengers) {
        this.drivers = passengers;
        this.size = passengers.size();
        this.total = passengers.size();
    }

    public DriversListResponse(List<DriverResponse> passengers, String field) {
        this.drivers = passengers;
        this.size = passengers.size();
        this.total = passengers.size();
        this.sortedByField = field;
    }

    public DriversListResponse(List<DriverResponse> passengers, Integer page, Integer total) {
        this.drivers = passengers;
        this.size = passengers.size();
        this.total = total;
        this.page = page;
    }

    public DriversListResponse(List<DriverResponse> passengers, Integer page, Integer total, String field) {
        this.drivers = passengers;
        this.size = passengers.size();
        this.total = total;
        this.page = page;
        this.sortedByField = field;
    }
}
