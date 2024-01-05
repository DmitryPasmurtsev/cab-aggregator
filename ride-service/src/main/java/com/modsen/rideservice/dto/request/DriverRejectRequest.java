package com.modsen.rideservice.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverRejectRequest {
    List<UserIdRequest> refusedDrivers;
}
