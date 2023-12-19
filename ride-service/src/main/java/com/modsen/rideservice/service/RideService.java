package com.modsen.rideservice.service;

import com.modsen.rideservice.dto.request.DriverFinishRequest;
import com.modsen.rideservice.dto.request.PassengerFinishRequest;
import com.modsen.rideservice.dto.request.RideCreationRequest;
import com.modsen.rideservice.dto.request.UserIdRequest;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.dto.response.RidesListResponse;

public interface RideService {

    RideResponse getById(Long id);

    void deleteRide(Long id);

    RideResponse addRide(RideCreationRequest dto);

    void finishRide(Long id, DriverFinishRequest dto);

    void finishRide(Long id, PassengerFinishRequest dto);

    void startRide(Long id, UserIdRequest dto);

    void passengerRejectRide(Long id, UserIdRequest dto);

    void driverRejectRide(Long id, UserIdRequest dto);

    RidesListResponse getAllRidesForPassenger(Long id);

    RidesListResponse getAllRidesForPassenger(Long id, Integer offset, Integer page, String field);

    RidesListResponse getAllRidesForPassenger(Long id, Integer offset, Integer page);

    RidesListResponse getAllRidesForPassenger(Long id, String field);
}
