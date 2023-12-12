package com.modsen.passengerservice.service.impl;

import com.modsen.passengerservice.dto.request.PassengerCreationRequest;
import com.modsen.passengerservice.dto.response.PassengerResponse;
import com.modsen.passengerservice.dto.response.PassengersListResponse;
import com.modsen.passengerservice.entity.Passenger;
import com.modsen.passengerservice.exceptions.NotCreatedException;
import com.modsen.passengerservice.exceptions.NotFoundException;
import com.modsen.passengerservice.repository.PassengerRepository;
import com.modsen.passengerservice.service.PassengerService;
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
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;
    private final ModelMapper modelMapper;

    private PassengerResponse toDTO(Passenger passenger) {
        if (passenger == null) return null;
        PassengerResponse dto = modelMapper.map(passenger, PassengerResponse.class);
        dto.setRating(getRatingById(dto.getId()));
        return dto;
    }

    private Passenger toModel(PassengerCreationRequest passenger) {
        return modelMapper.map(passenger, Passenger.class);
    }

    public PassengersListResponse getList() {
        List<PassengerResponse> passengers = passengerRepository.findAll().stream().map(this::toDTO).toList();
        return new PassengersListResponse(passengers);
    }

    public PassengerResponse getById(Long id) {
        Optional<Passenger> optionalPassenger = passengerRepository.findById(id);
        if (optionalPassenger.isPresent()) return optionalPassenger.map(this::toDTO).get();
        else throw new NotFoundException("Passenger with id={" + id + "} not found");
    }

    public void deletePassenger(Long id) {
        checkExistence(id);
        passengerRepository.deleteById(id);
    }

    public PassengerResponse addPassenger(PassengerCreationRequest dto) {
        checkConstraints(null, dto);
        return toDTO(passengerRepository.save(toModel(dto)));
    }

    public PassengerResponse updatePassenger(Long id, PassengerCreationRequest dto) {
        checkExistence(id);
        checkConstraints(id, dto);
        Passenger passenger = toModel(dto);
        passenger.setId(id);
        return toDTO(passengerRepository.save(passenger));
    }

    private void checkConstraints(Long id, PassengerCreationRequest dto) {
        PassengerResponse passengerByEmail = getByEmail(dto.getEmail());
        PassengerResponse passengerByPhone = getByPhone(dto.getPhone());
        if (passengerByEmail != null && !Objects.equals(passengerByEmail.getId(), id))
            throw new NotCreatedException("Passenger with email={" + dto.getEmail() + "} is already exists");
        if (passengerByPhone != null && !Objects.equals(passengerByPhone.getId(), id))
            throw new NotCreatedException("Passenger with phone={" + dto.getPhone() + "} is already exists");
    }

    private void checkExistence(Long id) {
        if (passengerRepository.findById(id).isEmpty())
            throw new NotFoundException("Passenger with id={" + id + "} not found");
    }

    public PassengerResponse getByEmail(String email) {
        return toDTO(passengerRepository.findPassengerByEmail(email));
    }

    public PassengerResponse getByPhone(String phone) {
        return toDTO(passengerRepository.findPassengerByPhone(phone));
    }

    public PassengersListResponse getListWithPaginationAndSort(Integer offset, Integer page, String field) {
        Page<PassengerResponse> pageRequest = passengerRepository.findAll(PageRequest.of(page, offset).withSort(Sort.by(field))).map(this::toDTO);
        return new PassengersListResponse(pageRequest.getContent(), pageRequest.getPageable().getPageNumber(), (int) pageRequest.getTotalElements(), field);
    }

    public PassengersListResponse getListWithPagination(Integer offset, Integer page) {
        Page<PassengerResponse> pageRequest = passengerRepository.findAll(PageRequest.of(page, offset)).map(this::toDTO);
        return new PassengersListResponse(pageRequest.getContent(), pageRequest.getPageable().getPageNumber(), (int) pageRequest.getTotalElements());
    }

    public PassengersListResponse getListWithSort(String field) {
        return new PassengersListResponse(passengerRepository.findAll(Sort.by(field)).stream().map(this::toDTO).toList(), field);
    }

    public Double getRatingById(Long id) {
        checkExistence(id);
        // в будущем здесь будет обращение к сервису рейтингов для получения рейтинга пассажира
        double rating = new Random().nextDouble(1, 5);
        rating = Math.round(rating * 10) / 10.0;
        return rating;
    }
}
