package com.modsen.passengerservice.service;

import com.modsen.passengerservice.dto.PassengerCreationRequest;
import com.modsen.passengerservice.dto.PassengerResponse;
import com.modsen.passengerservice.dto.PassengersListResponse;
import com.modsen.passengerservice.entity.Passenger;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public interface PassengerService {
    PassengersListResponse getList();
    PassengerResponse getDTOById(Long id);
    Passenger getEntityById(Long id);
    void deletePassenger(Long id);
    PassengerResponse addPassenger(PassengerCreationRequest dto);
    void updatePassenger(Long id, PassengerCreationRequest dto);
    Page<PassengerResponse> getListWithPaginationAndSort(Integer offset, Integer page, String field);
    Page<PassengerResponse> getListWithPagination(Integer offset, Integer page);
    PassengersListResponse getListWithSort(String field);
}
