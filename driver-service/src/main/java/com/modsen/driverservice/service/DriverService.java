package com.modsen.driverservice.service;

import com.modsen.driverservice.dto.request.AvailabilityStatusDto;
import com.modsen.driverservice.dto.request.DriverCreationRequest;
import com.modsen.driverservice.dto.request.RatingUpdateDto;
import com.modsen.driverservice.dto.response.DriverResponse;
import com.modsen.driverservice.dto.response.DriversListResponse;

public interface DriverService {
    DriversListResponse getAllDrivers();

    DriversListResponse getBlockedDrivers();

    DriversListResponse getAvailableDrivers();

    DriverResponse getById(Long id);

    void blockDriver(Long id);

    DriverResponse addDriver(DriverCreationRequest dto);

    DriverResponse updateDriver(Long id, DriverCreationRequest dto);

    DriversListResponse getAllDrivers(Integer offset, Integer page, String field);

    DriversListResponse getAllDrivers(Integer offset, Integer page);

    DriversListResponse getAllDrivers(String field);

    DriversListResponse getAvailableDrivers(Integer offset, Integer page, String field);

    DriversListResponse getAvailableDrivers(Integer offset, Integer page);

    DriversListResponse getAvailableDrivers(String field);

    void changeAvailabilityStatus(Long id, AvailabilityStatusDto dto);

    void updateRating(RatingUpdateDto dto);

    void findAvailableDriver();
}
