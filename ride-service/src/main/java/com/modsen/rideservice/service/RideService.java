package com.modsen.rideservice.service;

import com.modsen.rideservice.dto.request.*;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.dto.response.RidesListResponse;
import org.springframework.stereotype.Component;

@Component
public interface RideService {

    RideResponse getById(Long id);

    void deleteRide(Long id);

    RideResponse addRide(RideCreationRequest dto);

    void changeStatus(Long id, UserIdRequest dto);

    void finishRide(Long id, DriverFinishRequest dto);

    void finishRide(Long id, PassengerFinishRequest dto);

    void startRide(Long id, UserIdRequest dto);

    void rejectRide(Long id, UserIdRequest dto);

    void rejectRide(Long id, DriverRejectRequest dto);

    RidesListResponse getAllRidesForPassenger(Long id);

    RidesListResponse getAllRidesForPassenger(Long id, Integer offset, Integer page, String field);

    RidesListResponse getAllRidesForPassenger(Long id, Integer offset, Integer page);

    RidesListResponse getAllRidesForPassenger(Long id, String field);
}
