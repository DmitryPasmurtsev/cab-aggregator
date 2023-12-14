package com.modsen.rideservice.service;

import com.modsen.rideservice.dto.request.DriverFinishRequest;
import com.modsen.rideservice.dto.request.PassengerFinishRequest;
import com.modsen.rideservice.dto.request.RideCreationRequest;
import com.modsen.rideservice.dto.request.UserIdRequest;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.dto.response.RidesListResponse;
import org.springframework.stereotype.Component;

@Component
public interface RideService {

    RideResponse getById(Long id);

    void deleteRide(Long id);

    void addRide(RideCreationRequest dto);

    void changeStatus(Long id, UserIdRequest dto);

    void finishRide(Long id, DriverFinishRequest dto);

    void finishRide(Long id, PassengerFinishRequest dto);

    RidesListResponse getAllRidesForPassenger(UserIdRequest dto);

    RidesListResponse getAllRidesForPassenger(UserIdRequest dto, Integer offset, Integer page, String field);

    RidesListResponse getAllRidesForPassenger(UserIdRequest dto, Integer offset, Integer page);

    RidesListResponse getAllRidesForPassenger(UserIdRequest dto, String field);
}
