package com.modsen.passengerservice.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PassengersListResponse {
    Integer page;
    Integer size;
    Integer total;
    String sortedByField;
    List<PassengerResponse> passengers;
}
