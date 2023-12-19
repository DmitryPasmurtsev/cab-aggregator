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

import javax.swing.text.html.Option;
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
        PassengerResponse dto = modelMapper.map(passenger, PassengerResponse.class);
        dto.setRating(getRatingById(dto.getId()));
        return dto;
    }

    private Passenger toModel(PassengerCreationRequest passenger) {
        return modelMapper.map(passenger, Passenger.class);
    }

    public PassengersListResponse getList() {
        List<PassengerResponse> passengers = passengerRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
        return PassengersListResponse.builder()
                .passengers(passengers)
                .size(passengers.size())
                .total(passengers.size())
                .build();
    }

    public PassengerResponse getById(Long id) {
        Optional<Passenger> optionalPassenger = passengerRepository.findById(id);
        if (optionalPassenger.isPresent()) return optionalPassenger
                .map(this::toDTO)
                .get();
        else throw new NotFoundException("id", "Passenger with id={" + id + "} not found");
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
        Optional<Passenger> passengerByEmail = getEntityByEmail(dto.getEmail());
        Optional<Passenger> passengerByPhone = getEntityByPhone(dto.getPhone());
        if (passengerByEmail.isPresent() && !Objects.equals(passengerByEmail.get().getId(), id))
            throw new NotCreatedException("email", "Passenger with email={" + dto.getEmail() + "} is already exists");
        if (passengerByPhone.isPresent() && !Objects.equals(passengerByPhone.get().getId(), id))
            throw new NotCreatedException("phone", "Passenger with phone={" + dto.getPhone() + "} is already exists");
    }

    private Optional<Passenger> getEntityByEmail(String email) {
        return passengerRepository.findPassengerByEmail(email);
    }

    private Optional<Passenger> getEntityByPhone(String phone) {
        return passengerRepository.findPassengerByPhone(phone);
    }

    private void checkExistence(Long id) {
        if (!passengerRepository.existsById(id))
            throw new NotFoundException("id", "Passenger with id={" + id + "} not found");
    }

    public PassengersListResponse getListWithPaginationAndSort(Integer offset, Integer page, String field) {
        Page<PassengerResponse> responsePage = passengerRepository.findAll(PageRequest.of(page, offset)
                        .withSort(Sort.by(field)))
                .map(this::toDTO);
        return PassengersListResponse.builder()
                .passengers(responsePage.getContent())
                .size(responsePage.getContent().size())
                .page(responsePage.getPageable().getPageNumber())
                .total((int) responsePage.getTotalElements())
                .sortedByField(field)
                .build();
    }

    public PassengersListResponse getListWithPagination(Integer offset, Integer page) {
        Page<PassengerResponse> responsePage = passengerRepository.findAll(PageRequest.of(page, offset))
                .map(this::toDTO);
        return PassengersListResponse.builder()
                .passengers(responsePage.getContent())
                .size(responsePage.getContent().size())
                .page(responsePage.getPageable().getPageNumber())
                .total((int) responsePage.getTotalElements())
                .build();
    }

    public PassengersListResponse getListWithSort(String field) {
        List<PassengerResponse> responseList = passengerRepository.findAll(Sort.by(field))
                .stream()
                .map(this::toDTO)
                .toList();
        return PassengersListResponse.builder()
                .passengers(responseList)
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
}
