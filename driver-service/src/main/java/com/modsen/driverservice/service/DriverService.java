package com.modsen.driverservice.service;

import com.modsen.driverservice.dto.DriverCreationRequest;
import com.modsen.driverservice.dto.DriverResponse;
import com.modsen.driverservice.dto.DriversListResponse;
import com.modsen.driverservice.entity.Driver;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public interface DriverService {
    DriversListResponse getAllDrivers();

    DriversListResponse getAvailableDrivers();

    DriverResponse getDTOById(Long id);

    Driver getEntityById(Long id);

    void deleteDriver(Long id);

    DriverResponse addDriver(DriverCreationRequest dto);

    void updateDriver(Long id, DriverCreationRequest dto);

    Page<DriverResponse> getAllDrivers(Integer offset, Integer page, String field);

    Page<DriverResponse> getAllDrivers(Integer offset, Integer page);

    DriversListResponse getAllDrivers(String field);

    Page<DriverResponse> getAvailableDrivers(Integer offset, Integer page, String field);

    Page<DriverResponse> getAvailableDrivers(Integer offset, Integer page);

    DriversListResponse getAvailableDrivers(String field);

    Double getRatingById(Long id);

    boolean changeAvailabilityStatus(Long id);
}
