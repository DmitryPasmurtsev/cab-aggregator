package com.modsen.rideservice.service.impl;

import com.modsen.rideservice.dto.request.*;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.dto.response.RidesListResponse;
import com.modsen.rideservice.entity.Rating;
import com.modsen.rideservice.entity.Ride;
import com.modsen.rideservice.enums.Status;
import com.modsen.rideservice.exceptions.NoAccessException;
import com.modsen.rideservice.exceptions.NotCreatedException;
import com.modsen.rideservice.exceptions.NotFoundException;
import com.modsen.rideservice.exceptions.WrongStatusException;
import com.modsen.rideservice.repository.RideRepository;
import com.modsen.rideservice.service.RideService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Driver;
import java.util.*;

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
    public RideResponse addRide(RideCreationRequest dto) {
        Ride ride = toModel(dto);
        ride.setCost(Math.round(new Random().nextDouble(5, 20)*100)/100.0);
        ride.setStatus(Status.NOT_ACCEPTED);
        ride.setDriverId(getAvailableDriverId(null));
        ride.setStatus(Status.ACCEPTED);
        return toDTO(rideRepository.save(ride));
    }

    private Ride getEntityById(Long id) {
        Optional<Ride> ride = rideRepository.findById(id);
        if(ride.isPresent()) return ride.get();
        throw new NotFoundException("id", "Ride with id={"+id+"} not found");
    }

    private Long getAvailableDriverId(DriverRejectRequest dto) {
        //здесь будет отправка запроса на сервис водителей для получения свободного
        //временно реализовано рандомное генерирование id водителя
        return new Random().nextLong(1, 10);
    }

    public void finishRide(Long id, DriverFinishRequest dto) {
        Ride ride = getEntityById(id);
        if(ride.getStatus()!=Status.STARTED)
            throw new WrongStatusException("status","Ride with id={"+id+"} not started");
        ride.setStatus(Status.FINISHED);
        Rating rating = Rating.builder()
                .ride(ride)
                .passengerRating(dto.getRatingToPassenger())
                .build();
        ride.setRating(rating);
        rideRepository.save(ride);
    }

    public void finishRide(Long id, PassengerFinishRequest dto) {
        Ride ride = getEntityById(id);
        if (dto.getRatingToDriver()!=null) ride.getRating().setDriverRating(dto.getRatingToDriver());
        rideRepository.save(ride);
    }

    public void startRide(Long id, UserIdRequest dto) {
        Ride ride = getEntityById(id);
        checkDriverAccess(ride, dto.getUserId());
        if(ride.getStatus()!=Status.ACCEPTED)
            throw new WrongStatusException("status","Ride with id={"+id+"} has wrong status");
        ride.setStatus(Status.STARTED);
        rideRepository.save(ride);
    }

    public void passengerRejectRide(Long id, UserIdRequest dto) {
        Ride ride = getEntityById(id);
        checkPassengerAccess(ride, dto.getUserId());
        if(ride.getStatus().getValue()>Status.ACCEPTED.getValue())
            throw new WrongStatusException("status","Ride with id={"+id+"} can`t be rejected");
        ride.setStatus(Status.REJECTED);
        rideRepository.save(ride);
    }

    public void driverRejectRide(Long id, UserIdRequest dto) {
        Ride ride = getEntityById(id);
        checkDriverAccess(ride, dto.getUserId());
        if(ride.getStatus().getValue()>Status.ACCEPTED.getValue())
            throw new WrongStatusException("status","Ride with id={"+id+"} can`t be rejected");
        ride.setStatus(Status.NOT_ACCEPTED);
        ride.setDriverId(getAvailableDriverId(null));
        ride.setStatus(Status.ACCEPTED);
        rideRepository.save(ride);
    }

    public RidesListResponse getAllRidesForPassenger(Long id) {
        List<RideResponse> rides = rideRepository.findAllByPassengerId(id).stream()
                .map(this::toDTO)
                .toList();
        return RidesListResponse.builder()
                .rides(rides)
                .size(rides.size())
                .total(rides.size())
                .build();
    }

    @Override
    public RidesListResponse getAllRidesForPassenger(Long id, Integer offset, Integer page, String field) {
        Page<RideResponse> ridesPage = rideRepository.findAllByPassengerId(id, PageRequest.of(page, offset).withSort(Sort.by(field)))
                .map(this::toDTO);
        return RidesListResponse.builder()
                .rides(ridesPage.getContent())
                .size(ridesPage.getContent().size())
                .page(ridesPage.getPageable().getPageNumber())
                .total((int) ridesPage.getTotalElements())
                .sortedByField(field)
                .build();
    }

    @Override
    public RidesListResponse getAllRidesForPassenger(Long id, Integer offset, Integer page) {
        Page<RideResponse> ridesPage = rideRepository.findAllByPassengerId(id, PageRequest.of(page, offset)).map(this::toDTO);
        return RidesListResponse.builder()
                .rides(ridesPage.getContent())
                .size(ridesPage.getContent().size())
                .page(ridesPage.getPageable().getPageNumber())
                .total((int) ridesPage.getTotalElements())
                .build();
    }

    @Override
    public RidesListResponse getAllRidesForPassenger(Long id, String field) {
        List<RideResponse> ridesList = rideRepository.findAllByPassengerId(id, Sort.by(field)).stream()
                .map(this::toDTO)
                .toList();
        return RidesListResponse.builder()
                .rides(ridesList)
                .size(ridesList.size())
                .total(ridesList.size())
                .sortedByField(field)
                .build();
    }

    public RideResponse getById(Long id) {
        return toDTO(getEntityById(id));
    }

    private void checkPassengerAccess(Ride ride, Long passengerId) {
        if(!Objects.equals(ride.getPassengerId(), passengerId))
            throw new NoAccessException("userId","Passenger with id={"+passengerId+"} has no access to ride with id={"+ride.getId()+"}");
    }

    private void checkDriverAccess(Ride ride, Long driverId) {
        if(!Objects.equals(ride.getDriverId(), driverId))
            throw new NoAccessException("userId","Driver with id={"+driverId+"} has no access to ride with id={"+ride.getId()+"}");
    }


    private void checkExistence(Long id) {
        if (!rideRepository.existsById(id)) throw new NotFoundException("id", "Ride with id={" + id + "} not found");
    }
}