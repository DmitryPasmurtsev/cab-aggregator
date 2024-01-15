package com.modsen.passengerservice.service;

import com.modsen.passengerservice.dto.request.PassengerCreationRequest;
import com.modsen.passengerservice.dto.request.RatingUpdateDto;
import com.modsen.passengerservice.dto.response.PassengerResponse;
import com.modsen.passengerservice.dto.response.PassengersListResponse;

public interface PassengerService {
    PassengerResponse getById(Long id);

    void blockPassenger(Long id);

    PassengerResponse addPassenger(PassengerCreationRequest dto);

    PassengerResponse updatePassenger(Long id, PassengerCreationRequest dto);

    PassengersListResponse getPassengersList(Integer offset, Integer page, String field);

    PassengersListResponse getBlockedPassengersList();

    void updateRating(RatingUpdateDto dto);
}
