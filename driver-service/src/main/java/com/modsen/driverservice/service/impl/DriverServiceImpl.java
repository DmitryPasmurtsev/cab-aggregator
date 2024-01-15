package com.modsen.driverservice.service.impl;

import com.modsen.driverservice.dto.request.AvailabilityStatusDto;
import com.modsen.driverservice.dto.request.DriverCreationRequest;
import com.modsen.driverservice.dto.request.RatingUpdateDto;
import com.modsen.driverservice.dto.response.DriverResponse;
import com.modsen.driverservice.dto.response.DriversListResponse;
import com.modsen.driverservice.entity.Driver;
import com.modsen.driverservice.exceptions.NotAvailableDriverException;
import com.modsen.driverservice.exceptions.NotCreatedException;
import com.modsen.driverservice.exceptions.NotFoundException;
import com.modsen.driverservice.feign.client.DriverAvailabilityClient;
import com.modsen.driverservice.feign.dto.DriverAvailabilityCheckDto;
import com.modsen.driverservice.kafka.AvailableDriverProducer;
import com.modsen.driverservice.repository.DriverRepository;
import com.modsen.driverservice.service.DriverService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DriverServiceImpl implements DriverService {
    private final DriverRepository driverRepository;
    private final ModelMapper modelMapper;
    private final DriverAvailabilityClient driverAvailabilityClient;
    private final AvailableDriverProducer availableDriverProducer;

    private DriverResponse toDTO(Driver driver) {
        return modelMapper.map(driver, DriverResponse.class);
    }

    private Driver toModel(DriverCreationRequest driver) {
        return modelMapper.map(driver, Driver.class);
    }

    public DriversListResponse getAllDrivers() {
        List<DriverResponse> drivers = driverRepository.findAllByIsBlockedIsFalse()
                .stream()
                .map(this::toDTO)
                .toList();
        return DriversListResponse.builder()
                .drivers(drivers)
                .size(drivers.size())
                .total(drivers.size())
                .build();
    }

    public DriversListResponse getBlockedDrivers() {
        List<DriverResponse> drivers = driverRepository.findAllByIsBlockedIsTrue()
                .stream()
                .map(this::toDTO)
                .toList();
        return DriversListResponse.builder()
                .drivers(drivers)
                .size(drivers.size())
                .total(drivers.size())
                .build();
    }

    public DriversListResponse getAvailableDrivers() {
        List<DriverResponse> drivers = driverRepository.findAllByIsAvailableIsTrueAndIsBlockedIsFalse()
                .stream()
                .map(this::toDTO)
                .toList();
        return DriversListResponse.builder()
                .drivers(drivers)
                .size(drivers.size())
                .total(drivers.size())
                .build();
    }

    public DriverResponse getById(Long id) {
        return toDTO(getEntityById(id));
    }

    private Driver getEntityById(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("id", "Driver with id={" + id + "} not found"));
    }

    private Driver getNotBlockedEntityById(Long id) {
        return driverRepository.findByIdAndIsBlockedIsFalse(id)
                .orElseThrow(() -> new NotFoundException("id", "Driver with id={" + id + "} not found"));
    }

    public void blockDriver(Long id) {
        Driver driver = getNotBlockedEntityById(id);
        checkPossibilityToChangeStatus(id);

        driver.setBlocked(true);
        driver.setAvailable(false);

        driverRepository.save(driver);
    }

    public DriverResponse addDriver(DriverCreationRequest dto) {
        checkConstraints(null, dto);
        return toDTO(driverRepository.save(toModel(dto)));
    }

    public DriverResponse updateDriver(Long id, DriverCreationRequest dto) {
        Driver driver = getNotBlockedEntityById(id);
        checkConstraints(id, dto);

        driver.setName(dto.getName());
        driver.setSurname(dto.getSurname());
        driver.setPhone(dto.getPhone());

        return toDTO(driverRepository.save(driver));
    }

    private void checkConstraints(Long id, DriverCreationRequest dto) {
        Optional<Driver> passengerByPhone = getNotBlockedEntityByPhone(dto.getPhone());
        if (passengerByPhone.isPresent() && !Objects.equals(passengerByPhone.get().getId(), id))
            throw new NotCreatedException("phone", "Driver with phone={" + dto.getPhone() + "} is already exists");
    }

    private Optional<Driver> getNotBlockedEntityByPhone(String phone) {
        return driverRepository.findDriverByPhoneAndIsBlockedIsFalse(phone);
    }

    public DriversListResponse getAllDrivers(Integer offset, Integer page, String field) {
        Page<DriverResponse> responsePage = driverRepository.findAllByIsBlockedIsFalse(PageRequest.of(page, offset)
                        .withSort(Sort.by(field)))
                .map(this::toDTO);
        return DriversListResponse.builder()
                .drivers(responsePage.getContent())
                .size(responsePage.getContent().size())
                .page(responsePage.getPageable().getPageNumber())
                .total((int) responsePage.getTotalElements())
                .sortedByField(field)
                .build();
    }

    public DriversListResponse getAllDrivers(Integer offset, Integer page) {
        Page<DriverResponse> responsePage = driverRepository.findAllByIsBlockedIsFalse(PageRequest.of(page, offset))
                .map(this::toDTO);
        return DriversListResponse.builder()
                .drivers(responsePage.getContent())
                .size(responsePage.getContent().size())
                .page(responsePage.getPageable().getPageNumber())
                .total((int) responsePage.getTotalElements())
                .build();
    }

    public DriversListResponse getAllDrivers(String field) {
        List<DriverResponse> responseList = driverRepository.findAllByIsBlockedIsFalse(Sort.by(field))
                .stream()
                .map(this::toDTO)
                .toList();
        return DriversListResponse.builder()
                .drivers(responseList)
                .size(responseList.size())
                .sortedByField(field)
                .build();
    }

    public DriversListResponse getAvailableDrivers(Integer offset, Integer page, String field) {
        Page<DriverResponse> responsePage = driverRepository.findAllByIsAvailableIsTrueAndIsBlockedIsFalse(PageRequest.of(page, offset)
                        .withSort(Sort.by(field)))
                .map(this::toDTO);
        return DriversListResponse.builder()
                .drivers(responsePage.getContent())
                .size(responsePage.getContent().size())
                .page(responsePage.getPageable().getPageNumber())
                .total((int) responsePage.getTotalElements())
                .sortedByField(field)
                .build();
    }

    public DriversListResponse getAvailableDrivers(Integer offset, Integer page) {
        Page<DriverResponse> responsePage = driverRepository.findAllByIsAvailableIsTrueAndIsBlockedIsFalse(PageRequest.of(page, offset))
                .map(this::toDTO);
        return DriversListResponse.builder()
                .drivers(responsePage.getContent())
                .size(responsePage.getContent().size())
                .page(responsePage.getPageable().getPageNumber())
                .total((int) responsePage.getTotalElements())
                .build();
    }

    public DriversListResponse getAvailableDrivers(String field) {
        List<DriverResponse> responseList = driverRepository.findAllByIsAvailableIsTrueAndIsBlockedIsFalse(Sort.by(field))
                .stream()
                .map(this::toDTO)
                .toList();
        return DriversListResponse.builder()
                .drivers(responseList)
                .size(responseList.size())
                .sortedByField(field)
                .build();
    }

    public void changeAvailabilityStatus(Long id, AvailabilityStatusDto dto) {
        Driver driver = getNotBlockedEntityById(id);

        if (!driver.isAvailable()) {
            checkPossibilityToChangeStatus(id);
        } else {
            availableDriverProducer.notifyDriverAvailability(id);
        }
        driver.setAvailable(dto.isAvailable());

        driverRepository.save(driver);
    }

    public void findAvailableDriver() {
        Optional<Driver> driverOptional = driverRepository.findFirstByIsAvailableIsTrueAndIsBlockedIsFalse();
        driverOptional.ifPresent(driver -> availableDriverProducer.notifyDriverAvailability(driver.getId()));
    }

    private void checkPossibilityToChangeStatus(Long id) {
        DriverAvailabilityCheckDto dto = driverAvailabilityClient.checkAvailability(id);
        if (!dto.isAvailable()) {
            throw new NotAvailableDriverException("Not finished ride", "Driver has not finished ride");
        }
    }

    public void updateRating(RatingUpdateDto dto) {
        Driver driver = getEntityById(dto.getUserId());
        driver.setRating(dto.getRating());
        driverRepository.save(driver);
    }
}
