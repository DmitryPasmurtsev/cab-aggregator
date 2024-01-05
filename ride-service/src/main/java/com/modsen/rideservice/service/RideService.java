package com.modsen.rideservice.service;

import com.modsen.rideservice.dto.request.DriverFinishRequest;
import com.modsen.rideservice.dto.request.PassengerFinishRequest;
import com.modsen.rideservice.dto.request.RideCreationRequest;
import com.modsen.rideservice.dto.request.UserIdRequest;
import com.modsen.rideservice.dto.response.DriverAvailabilityCheckDto;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.dto.response.RidesListResponse;
import com.modsen.rideservice.dto.response.StringResponse;

public interface RideService {
    RideResponse getById(Long id);

    StringResponse deleteRide(Long id);

    RideResponse addRide(RideCreationRequest dto);

    StringResponse finishRide(Long id, DriverFinishRequest dto);

    StringResponse finishRide(Long id, PassengerFinishRequest dto);

    StringResponse startRide(Long id, UserIdRequest dto);

    StringResponse passengerRejectRide(Long id, UserIdRequest dto);

    StringResponse driverRejectRide(Long id, UserIdRequest dto);

    RidesListResponse getList(Long id, Integer offset, Integer page, String field);

    void setDriverToRide(Long driverId);

    DriverAvailabilityCheckDto checkDriverAvailability(Long id);
}
