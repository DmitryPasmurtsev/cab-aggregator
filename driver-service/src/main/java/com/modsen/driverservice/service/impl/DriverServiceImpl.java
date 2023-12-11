package com.modsen.driverservice.service.impl;

import com.modsen.driverservice.dto.DriverCreationRequest;
import com.modsen.driverservice.dto.DriverResponse;
import com.modsen.driverservice.dto.DriversListResponse;
import com.modsen.driverservice.entity.Driver;
import com.modsen.driverservice.exceptions.NotCreatedException;
import com.modsen.driverservice.exceptions.NotFoundException;
import com.modsen.driverservice.repository.DriverRepository;
import com.modsen.driverservice.service.DriverService;
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
public class DriverServiceImpl implements DriverService {
    private final DriverRepository driverRepository;
    private final ModelMapper modelMapper;

    public DriverServiceImpl(DriverRepository driverRepository, ModelMapper modelMapper) {
        this.driverRepository = driverRepository;
        this.modelMapper = modelMapper;
    }

    private DriverResponse toDTO(Driver driver) {
        DriverResponse dto = modelMapper.map(driver, DriverResponse.class);
        dto.setRating(getRatingById(dto.getId()));
        return dto;
    }

    private Driver toModel(DriverCreationRequest driver) {
        return modelMapper.map(driver, Driver.class);
    }

    public DriversListResponse getAllDrivers() {
        List<DriverResponse> passengers = driverRepository.findAll().stream().map(this::toDTO).toList();
        return new DriversListResponse(passengers);
    }

    public DriversListResponse getAvailableDrivers() {
        List<DriverResponse> drivers = driverRepository.findAllByIsAvailableIsTrue().stream().map(this::toDTO).toList();
        return new DriversListResponse(drivers);
    }

    public DriverResponse getDTOById(Long id) {
        Optional<Driver> optionalDriver = driverRepository.findById(id);
        if (optionalDriver.isPresent()) return optionalDriver.map(this::toDTO).get();
        else throw new NotFoundException("Driver with id={" + id + "} not found");
    }

    public Driver getEntityById(Long id) {
        Optional<Driver> optionalDriver = driverRepository.findById(id);
        if (optionalDriver.isPresent()) return optionalDriver.get();
        else throw new NotFoundException("Driver with id={" + id + "} not found");
    }

    public void deleteDriver(Long id) {
        checkExistence(id);
        driverRepository.deleteById(id);
    }

    public DriverResponse addDriver(DriverCreationRequest dto) {
        checkConstraints(null, dto);
        return toDTO(driverRepository.save(toModel(dto)));
    }

    public void updateDriver(Long id, DriverCreationRequest dto) {
        Driver driver = getEntityById(id);
        checkConstraints(id, dto);
        driver.setName(dto.getName());
        driver.setSurname(dto.getSurname());
        driver.setPhone(dto.getPhone());
        driverRepository.save(driver);
    }

    private void checkConstraints(Long id, DriverCreationRequest dto) {
        Driver passengerByPhone = getEntityByPhone(dto.getPhone());
        if (passengerByPhone != null && !Objects.equals(passengerByPhone.getId(), id))
            throw new NotCreatedException("Driver with phone={" + dto.getPhone() + "} is already exists");
    }

    public Driver getEntityByPhone(String phone) {
        return driverRepository.findPassengerByPhone(phone);
    }

    public Page<DriverResponse> getAllDrivers(Integer offset, Integer page, String field) {
        return driverRepository.findAll(PageRequest.of(page, offset).withSort(Sort.by(field))).map(this::toDTO);
    }

    public Page<DriverResponse> getAllDrivers(Integer offset, Integer page) {
        return driverRepository.findAll(PageRequest.of(page, offset)).map(this::toDTO);
    }

    public DriversListResponse getAllDrivers(String field) {
        return new DriversListResponse(driverRepository.findAll(Sort.by(field)).stream().map(this::toDTO).toList());
    }

    public Page<DriverResponse> getAvailableDrivers(Integer offset, Integer page, String field) {
        return driverRepository.findAllByIsAvailableIsTrue(PageRequest.of(page, offset).withSort(Sort.by(field))).map(this::toDTO);
    }

    public Page<DriverResponse> getAvailableDrivers(Integer offset, Integer page) {
        return driverRepository.findAllByIsAvailableIsTrue(PageRequest.of(page, offset)).map(this::toDTO);
    }

    public DriversListResponse getAvailableDrivers(String field) {
        return new DriversListResponse(driverRepository.findAllByIsAvailableIsTrue(Sort.by(field)).stream().map(this::toDTO).toList());
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
        getEntityById(id);
    }
}
