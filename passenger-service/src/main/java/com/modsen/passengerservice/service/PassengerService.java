package com.modsen.passengerservice.service;

import com.modsen.passengerservice.dto.request.PassengerCreationRequest;
import com.modsen.passengerservice.dto.response.PassengerResponse;
import com.modsen.passengerservice.dto.response.PassengersListResponse;
import org.springframework.stereotype.Component;

@Component
public interface PassengerService {
    PassengersListResponse getList();

    PassengerResponse getById(Long id);

    void deletePassenger(Long id);

    PassengerResponse addPassenger(PassengerCreationRequest dto);

    PassengerResponse updatePassenger(Long id, PassengerCreationRequest dto);

    PassengersListResponse getListWithPaginationAndSort(Integer offset, Integer page, String field);

    PassengersListResponse getListWithPagination(Integer offset, Integer page);

    PassengersListResponse getListWithSort(String field);

    Double getRatingById(Long id);
}
