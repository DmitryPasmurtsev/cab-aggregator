package com.modsen.rideservice.service.impl;

import com.modsen.rideservice.dto.request.DriverFinishRequest;
import com.modsen.rideservice.dto.request.PassengerFinishRequest;
import com.modsen.rideservice.dto.request.RideCreationRequest;
import com.modsen.rideservice.dto.request.UserIdRequest;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.dto.response.RidesListResponse;
import com.modsen.rideservice.entity.Ride;
import com.modsen.rideservice.repository.RideRepository;
import com.modsen.rideservice.service.RideService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {
    private final RideRepository rideRepository;
    private final ModelMapper modelMapper;


    private RideResponse toDTO(Ride ride) {
        if (ride == null) return null;
        return modelMapper.map(ride, RideResponse.class);
    }

    private Ride toModel(RideCreationRequest dto) {
        return modelMapper.map(dto, Ride.class);
    }

    public void deleteRide(Long id) {
        checkExistence(id);
        rideRepository.deleteById(id);
    }

    @Override
    public void addRide(RideCreationRequest dto) {

    }

    @Override
    public void changeStatus(Long id, UserIdRequest dto) {

    }

    @Override
    public void finishRide(Long id, DriverFinishRequest dto) {

    }

    @Override
    public void finishRide(Long id, PassengerFinishRequest dto) {

    }

    @Override
    public RidesListResponse getAllRidesForPassenger(UserIdRequest dto) {
        return null;
    }

    @Override
    public RidesListResponse getAllRidesForPassenger(UserIdRequest dto, Integer offset, Integer page, String field) {
        return null;
    }

    @Override
    public RidesListResponse getAllRidesForPassenger(UserIdRequest dto, Integer offset, Integer page) {
        return null;
    }

    @Override
    public RidesListResponse getAllRidesForPassenger(UserIdRequest dto, String field) {
        return null;
    }

    public DriversListResponse getAllDrivers() {
        List<DriverResponse> passengers = driverRepository.findAll().stream().map(this::toDTO).toList();
        return new DriversListResponse(passengers);
    }

    public DriversListResponse getAvailableDrivers() {
        List<DriverResponse> drivers = driverRepository.findAllByIsAvailableIsTrue().stream().map(this::toDTO).toList();
        return new DriversListResponse(drivers);
    }

    public DriverResponse getById(Long id) {
        Optional<Driver> optionalDriver = driverRepository.findById(id);
        if (optionalDriver.isPresent()) return optionalDriver.map(this::toDTO).get();
        else throw new NotFoundException("id", "Driver with id={" + id + "} not found");
    }

    public DriverResponse addDriver(DriverCreationRequest dto) {
        checkConstraints(null, dto);
        return toDTO(driverRepository.save(toModel(dto)));
    }

    public DriverResponse updateDriver(Long id, DriverCreationRequest dto) {
        Driver driver = checkExistence(id);
        checkConstraints(id, dto);
        driver.setName(dto.getName());
        driver.setSurname(dto.getSurname());
        driver.setPhone(dto.getPhone());
        return toDTO(driverRepository.save(driver));
    }

    private void checkConstraints(Long id, DriverCreationRequest dto) {
        DriverResponse passengerByPhone = getByPhone(dto.getPhone());
        if (passengerByPhone != null && !Objects.equals(passengerByPhone.getId(), id))
            throw new NotCreatedException("phone", "Driver with phone={" + dto.getPhone() + "} is already exists");
    }

    public DriverResponse getByPhone(String phone) {
        return toDTO(driverRepository.findPassengerByPhone(phone));
    }

    public DriversListResponse getAllDrivers(Integer offset, Integer page, String field) {
        Page<DriverResponse> responsePage = driverRepository.findAll(PageRequest.of(page, offset).withSort(Sort.by(field))).map(this::toDTO);
        return new DriversListResponse(responsePage.getContent(), responsePage.getPageable().getPageNumber(), (int) responsePage.getTotalElements(), field);
    }

    public DriversListResponse getAllDrivers(Integer offset, Integer page) {
        Page<DriverResponse> responsePage = driverRepository.findAll(PageRequest.of(page, offset)).map(this::toDTO);
        return new DriversListResponse(responsePage.getContent(), responsePage.getPageable().getPageNumber(), (int) responsePage.getTotalElements());
    }

    public DriversListResponse getAllDrivers(String field) {
        List<DriverResponse> responseList = driverRepository.findAll(Sort.by(field)).stream().map(this::toDTO).toList();
        return new DriversListResponse(responseList, field);
    }

    public DriversListResponse getAvailableDrivers(Integer offset, Integer page, String field) {
        Page<DriverResponse> responsePage = driverRepository.findAllByIsAvailableIsTrue(PageRequest.of(page, offset).withSort(Sort.by(field))).map(this::toDTO);
        return new DriversListResponse(responsePage.getContent(), responsePage.getPageable().getPageNumber(), (int) responsePage.getTotalElements(), field);
    }

    public DriversListResponse getAvailableDrivers(Integer offset, Integer page) {
        Page<DriverResponse> responsePage = driverRepository.findAllByIsAvailableIsTrue(PageRequest.of(page, offset)).map(this::toDTO);
        return new DriversListResponse(responsePage.getContent(), responsePage.getPageable().getPageNumber(), (int) responsePage.getTotalElements());
    }

    public DriversListResponse getAvailableDrivers(String field) {
        List<DriverResponse> responseList = driverRepository.findAllByIsAvailableIsTrue(Sort.by(field)).stream().map(this::toDTO).toList();
        return new DriversListResponse(responseList, field);
    }

    public Double getRatingById(Long id) {
        checkExistence(id);
        // в будущем здесь будет обращение к сервису рейтингов для получения рейтинга пассажира
        double rating = new Random().nextDouble(1, 5);
        rating = Math.round(rating * 10) / 10.0;
        return rating;
    }

    public boolean changeAvailabilityStatus(Long id) {
        Driver driver = checkExistence(id);
        driver.setAvailable(!driver.isAvailable());
        driverRepository.save(driver);
        return driver.isAvailable();
    }

    private Driver checkExistence(Long id) {
        Optional<Driver> optionalDriver = driverRepository.findById(id);
        if (optionalDriver.isPresent()) return optionalDriver.get();
        else throw new NotFoundException("id", "Driver with id={" + id + "} not found");
    }
}
