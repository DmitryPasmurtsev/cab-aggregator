package com.modsen.rideservice.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RidesListResponse {
    Integer page;
    Integer size;
    Integer total;
    String sortedByField;
    List<RideResponse> rides;
}
