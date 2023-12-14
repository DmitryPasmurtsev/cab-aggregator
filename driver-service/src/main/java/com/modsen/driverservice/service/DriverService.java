package com.modsen.driverservice.service;

import com.modsen.driverservice.dto.request.DriverCreationRequest;
import com.modsen.driverservice.dto.response.DriverResponse;
import com.modsen.driverservice.dto.response.DriversListResponse;
import org.springframework.stereotype.Service;

@Service
public interface DriverService {
    DriversListResponse getAllDrivers();

    DriversListResponse getAvailableDrivers();

    DriverResponse getById(Long id);

    void deleteDriver(Long id);

    DriverResponse addDriver(DriverCreationRequest dto);

    DriverResponse updateDriver(Long id, DriverCreationRequest dto);

    DriversListResponse getAllDrivers(Integer offset, Integer page, String field);

    DriversListResponse getAllDrivers(Integer offset, Integer page);

    DriversListResponse getAllDrivers(String field);

    DriversListResponse getAvailableDrivers(Integer offset, Integer page, String field);

    DriversListResponse getAvailableDrivers(Integer offset, Integer page);

    DriversListResponse getAvailableDrivers(String field);

    Double getRatingById(Long id);

    boolean changeAvailabilityStatus(Long id);
}
