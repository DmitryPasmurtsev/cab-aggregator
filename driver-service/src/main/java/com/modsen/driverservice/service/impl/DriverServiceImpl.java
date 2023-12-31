package com.modsen.driverservice.service.impl;

import com.modsen.driverservice.dto.request.DriverCreationRequest;
import com.modsen.driverservice.dto.response.DriverResponse;
import com.modsen.driverservice.dto.response.DriversListResponse;
import com.modsen.driverservice.entity.Driver;
import com.modsen.driverservice.exceptions.NotCreatedException;
import com.modsen.driverservice.exceptions.NotFoundException;
import com.modsen.driverservice.repository.DriverRepository;
import com.modsen.driverservice.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {
    private final DriverRepository driverRepository;
    private final ModelMapper modelMapper;

    private DriverResponse toDTO(Driver driver) {
        DriverResponse dto = modelMapper.map(driver, DriverResponse.class);
        dto.setRating(getRatingById(dto.getId()));
        return dto;
    }

    private Driver toModel(DriverCreationRequest driver) {
        return modelMapper.map(driver, Driver.class);
    }

    public DriversListResponse getAllDrivers() {
        List<DriverResponse> drivers = driverRepository.findAll()
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
        List<DriverResponse> drivers = driverRepository.findAllByIsAvailableIsTrue()
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

    public void deleteDriver(Long id) {
        checkExistence(id);
        driverRepository.deleteById(id);
    }

    public DriverResponse addDriver(DriverCreationRequest dto) {
        checkConstraints(null, dto);
        return toDTO(driverRepository.save(toModel(dto)));
    }

    public DriverResponse updateDriver(Long id, DriverCreationRequest dto) {
        checkExistence(id);
        Driver driver = getEntityById(id);
        checkConstraints(id, dto);
        driver.setName(dto.getName());
        driver.setSurname(dto.getSurname());
        driver.setPhone(dto.getPhone());
        return toDTO(driverRepository.save(driver));
    }

    private void checkConstraints(Long id, DriverCreationRequest dto) {
        Optional<Driver> passengerByPhone = getEntityByPhone(dto.getPhone());
        if (passengerByPhone.isPresent() && !Objects.equals(passengerByPhone.get().getId(), id))
            throw new NotCreatedException("phone", "Driver with phone={" + dto.getPhone() + "} is already exists");
    }

    private Optional<Driver> getEntityByPhone(String phone) {
        return driverRepository.findPassengerByPhone(phone);
    }

    public DriversListResponse getAllDrivers(Integer offset, Integer page, String field) {
        Page<DriverResponse> responsePage = driverRepository.findAll(PageRequest.of(page, offset)
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
        Page<DriverResponse> responsePage = driverRepository.findAll(PageRequest.of(page, offset))
                .map(this::toDTO);
        return DriversListResponse.builder()
                .drivers(responsePage.getContent())
                .size(responsePage.getContent().size())
                .page(responsePage.getPageable().getPageNumber())
                .total((int) responsePage.getTotalElements())
                .build();
    }

    public DriversListResponse getAllDrivers(String field) {
        List<DriverResponse> responseList = driverRepository.findAll(Sort.by(field))
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
        Page<DriverResponse> responsePage = driverRepository.findAllByIsAvailableIsTrue(PageRequest.of(page, offset)
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
        Page<DriverResponse> responsePage = driverRepository.findAllByIsAvailableIsTrue(PageRequest.of(page, offset))
                .map(this::toDTO);
        return DriversListResponse.builder()
                .drivers(responsePage.getContent())
                .size(responsePage.getContent().size())
                .page(responsePage.getPageable().getPageNumber())
                .total((int) responsePage.getTotalElements())
                .build();
    }

    public DriversListResponse getAvailableDrivers(String field) {
        List<DriverResponse> responseList = driverRepository.findAllByIsAvailableIsTrue(Sort.by(field))
                .stream()
                .map(this::toDTO)
                .toList();
        return DriversListResponse.builder()
                .drivers(responseList)
                .size(responseList.size())
                .sortedByField(field)
                .build();
    }

    public Double getRatingById(Long id) {
        checkExistence(id);
        // в будущем здесь будет обращение к сервису рейтингов для получения рейтинга пассажира
        double rating = new Random().nextDouble(1, 5);
        rating = Math.round(rating * 10) / 10.0;
        return rating;
    }

    public boolean changeAvailabilityStatus(Long id) {
        Driver driver = getEntityById(id);
        driver.setAvailable(!driver.isAvailable());
        driverRepository.save(driver);
        return driver.isAvailable();
    }

    private void checkExistence(Long id) {
        if (!driverRepository.existsById(id))
            throw new NotFoundException("id", "Driver with id={" + id + "} not found");
    }
}
