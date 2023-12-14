package com.modsen.rideservice.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RidesListResponse {
    Integer page;
    Integer size;
    Integer total;
    String sortedByField;
    List<RideResponse> rides;

    public RidesListResponse(List<RideResponse> rides) {
        this.rides = rides;
        this.size = rides.size();
        this.total = rides.size();
    }

    public RidesListResponse(List<RideResponse> rides, String field) {
        this.rides = rides;
        this.size = rides.size();
        this.total = rides.size();
        this.sortedByField = field;
    }

    public RidesListResponse(List<RideResponse> rides, Integer page, Integer total) {
        this.rides = rides;
        this.size = rides.size();
        this.total = total;
        this.page = page;
    }

    public RidesListResponse(List<RideResponse> rides, Integer page, Integer total, String field) {
        this.rides = rides;
        this.size = rides.size();
        this.total = total;
        this.page = page;
        this.sortedByField = field;
    }
}
