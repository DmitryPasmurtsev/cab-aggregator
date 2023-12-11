package com.modsen.passengerservice.service.impl;

import com.modsen.passengerservice.dto.*;
import com.modsen.passengerservice.entity.Passenger;
import com.modsen.passengerservice.exceptions.NotCreatedException;
import com.modsen.passengerservice.exceptions.NotFoundException;
import com.modsen.passengerservice.repository.PassengerRepository;
import com.modsen.passengerservice.service.PassengerService;
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
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;
    private final ModelMapper modelMapper;

    public PassengerServiceImpl(PassengerRepository passengerRepository, ModelMapper modelMapper) {
        this.passengerRepository = passengerRepository;
        this.modelMapper = modelMapper;
    }

    private PassengerResponse toDTO(Passenger passenger) {
        PassengerResponse dto = modelMapper.map(passenger, PassengerResponse.class);
        dto.setRating(getRatingById(dto.getId()));
        return dto;
    }
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
        checkConstraints(null, dto);
        return toDTO(passengerRepository.save(toModel(dto)));
    }

    public void updatePassenger(Long id, PassengerCreationRequest dto) {
        if(getEntityById(id)!=null) {
            checkConstraints(id, dto);
            Passenger passenger = toModel(dto);
            passenger.setId(id);
            passengerRepository.save(passenger);
        }
    }

    private void checkConstraints(Long id, PassengerCreationRequest dto) {
        Passenger passengerByEmail = getEntityByEmail(dto.getEmail());
        Passenger passengerByPhone = getEntityByPhone(dto.getPhone());
        if(passengerByEmail!=null && !Objects.equals(passengerByEmail.getId(), id)) throw new NotCreatedException("Passenger with email={"+dto.getEmail()+"} is already exists");
        if(passengerByPhone!=null && !Objects.equals(passengerByPhone.getId(), id)) throw new NotCreatedException("Passenger with phone={"+dto.getPhone()+"} is already exists");
    }

    public Passenger getEntityByEmail(String email) {
        return passengerRepository.findPassengerByEmail(email);
    }
    public Passenger getEntityByPhone(String phone) {
        return passengerRepository.findPassengerByPhone(phone);
    }

    public Page<PassengerResponse> getListWithPaginationAndSort(Integer offset, Integer page, String field) {
        return passengerRepository.findAll(PageRequest.of(page, offset).withSort(Sort.by(field))).map(this::toDTO);
    }

    public Page<PassengerResponse> getListWithPagination(Integer offset, Integer page) {
        return passengerRepository.findAll(PageRequest.of(page, offset)).map(this::toDTO);
    }

    public PassengersListResponse getListWithSort(String field) {
        return new PassengersListResponse(passengerRepository.findAll(Sort.by(field)).stream().map(this::toDTO).toList());
    }

    public Double getRatingById(Long id) {
        // в будущем здесь будет обращение к сервису рейтингов для получения рейтинга пассажира
        double rating = new Random().nextDouble(1, 5);
        rating=Math.round(rating*10)/10.0;
        return rating;
    }
}
