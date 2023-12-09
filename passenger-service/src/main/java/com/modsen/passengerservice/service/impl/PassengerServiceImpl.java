package com.modsen.passengerservice.service.impl;

import com.modsen.passengerservice.dto.*;
import com.modsen.passengerservice.entity.Passenger;
import com.modsen.passengerservice.exceptions.NotFoundException;
import com.modsen.passengerservice.repository.PassengerRepository;
import com.modsen.passengerservice.service.PassengerService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;
    private final ModelMapper modelMapper;

    public PassengerServiceImpl(PassengerRepository passengerRepository, ModelMapper modelMapper) {
        this.passengerRepository = passengerRepository;
        this.modelMapper = modelMapper;
    }

    private PassengerResponse toDTO(Passenger passenger) { return modelMapper.map(passenger, PassengerResponse.class);}
    private Passenger toModel(PassengerCreationRequest passenger) { return modelMapper.map(passenger, Passenger.class);}

    public PassengersListResponse getList() {
        List<PassengerResponse> passengers = passengerRepository.findAll().stream().map(this::toDTO).toList();
        return new PassengersListResponse(passengers);
    }

    public PassengerResponse getDTOById(Long id) {
        Optional<Passenger> optionalPassenger = passengerRepository.findById(id);
        if(optionalPassenger.isPresent()) return optionalPassenger.map(this::toDTO).get();
        else throw new NotFoundException("Пассажир с id " + id + " не найден");
    }

    public Passenger getEntityById(Long id) {
        Optional<Passenger> optionalPassenger = passengerRepository.findById(id);
        if(optionalPassenger.isPresent()) return optionalPassenger.get();
        else throw new NotFoundException("Пассажир с id " + id + " не найден");
    }

    public void deletePassenger(Long id) {
        if(getEntityById(id)!=null) passengerRepository.deleteById(id);
    }

    public PassengerResponse addPassenger(PassengerCreationRequest dto) {
        return toDTO(passengerRepository.save(toModel(dto)));
    }

    public void updatePassenger(Long id, PassengerCreationRequest dto) {
        Passenger passenger = toModel(dto);
        passenger.setId(id);
        passengerRepository.save(passenger);
    }
}
