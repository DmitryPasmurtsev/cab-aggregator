package com.modsen.passengerservice.service.impl;

import com.modsen.passengerservice.dto.request.PassengerCreationRequest;
import com.modsen.passengerservice.dto.request.RatingUpdateDto;
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

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;
    private final ModelMapper modelMapper;

    private PassengerResponse toDto(Passenger passenger) {
        return modelMapper.map(passenger, PassengerResponse.class);
    }

    private Passenger toModel(PassengerCreationRequest passenger) {
        return modelMapper.map(passenger, Passenger.class);
    }

    private PassengersListResponse getList() {
        List<PassengerResponse> passengers = passengerRepository.findAllByIsBlockedIsFalse()
                .stream()
                .map(this::toDto)
                .toList();
        return PassengersListResponse.builder()
                .passengers(passengers)
                .size(passengers.size())
                .total(passengers.size())
                .build();
    }

    public PassengerResponse getById(Long id) {
        return toDto(getEntityById(id));
    }

    private Passenger getEntityById(Long id) {
        return passengerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("id", "message.passenger.id.notFound"));
    }

    private Passenger getNotBlockedEntityById(Long id) {
        return passengerRepository.getByIdAndIsBlockedIsFalse(id)
                .orElseThrow(() -> new NotFoundException("id", "message.passenger.id.notFound"));
    }

    public void blockPassenger(Long id) {
        Passenger passenger = getNotBlockedEntityById(id);
        passenger.setBlocked(true);
        passengerRepository.save(passenger);
    }

    public PassengerResponse addPassenger(PassengerCreationRequest dto) {
        checkConstraints(null, dto);
        return toDto(passengerRepository.save(toModel(dto)));
    }

    public PassengerResponse updatePassenger(Long id, PassengerCreationRequest dto) {
        Passenger passenger = getNotBlockedEntityById(id);

        checkConstraints(id, dto);

        passenger.setName(dto.getName());
        passenger.setSurname(dto.getSurname());
        passenger.setEmail(dto.getEmail());
        passenger.setPhone(dto.getPhone());

        return toDto(passengerRepository.save(passenger));
    }

    private void checkConstraints(Long id, PassengerCreationRequest dto) {
        Optional<Passenger> passengerByEmail = getNotBlockedEntityByEmail(dto.getEmail());
        Optional<Passenger> passengerByPhone = getNotBlockedEntityByPhone(dto.getPhone());
        if (passengerByEmail.isPresent() && !Objects.equals(passengerByEmail.get().getId(), id)) {
            throw new NotCreatedException("email", "message.passenger.email.alreadyExists");
        }
        if (passengerByPhone.isPresent() && !Objects.equals(passengerByPhone.get().getId(), id)) {
            throw new NotCreatedException("phone", "message.passenger.phone.alreadyExists");
        }
    }

    private Optional<Passenger> getNotBlockedEntityByEmail(String email) {
        return passengerRepository.findPassengerByEmailAndIsBlockedIsFalse(email);
    }

    private Optional<Passenger> getNotBlockedEntityByPhone(String phone) {
        return passengerRepository.findPassengerByPhoneAndIsBlockedIsFalse(phone);
    }

    private PassengersListResponse getListWithPaginationAndSort(Integer limit, Integer page, String field) {
        Page<PassengerResponse> responsePage = passengerRepository.findAllByIsBlockedIsFalse(PageRequest.of(page, limit)
                        .withSort(Sort.by(field)))
                .map(this::toDto);
        return PassengersListResponse.builder()
                .passengers(responsePage.getContent())
                .size(responsePage.getContent().size())
                .page(responsePage.getPageable().getPageNumber())
                .total((int) responsePage.getTotalElements())
                .sortedByField(field)
                .build();
    }

    private PassengersListResponse getListWithPagination(Integer limit, Integer page) {
        Page<PassengerResponse> responsePage = passengerRepository.findAllByIsBlockedIsFalse(PageRequest.of(page, limit))
                .map(this::toDto);
        return PassengersListResponse.builder()
                .passengers(responsePage.getContent())
                .size(responsePage.getContent().size())
                .page(responsePage.getPageable().getPageNumber())
                .total((int) responsePage.getTotalElements())
                .build();
    }

    private PassengersListResponse getListWithSort(String field) {
        List<PassengerResponse> responseList = passengerRepository.findAllByIsBlockedIsFalse(Sort.by(field))
                .stream()
                .map(this::toDto)
                .toList();
        return PassengersListResponse.builder()
                .passengers(responseList)
                .size(responseList.size())
                .sortedByField(field)
                .total(responseList.size())
                .build();
    }

    public PassengersListResponse getPassengersList(Integer limit, Integer page, String field) {
        if (limit != null && page != null && field != null) {
            return getListWithPaginationAndSort(limit, page, field);
        } else if (limit != null && page != null) {
            return getListWithPagination(limit, page);
        } else if (field != null) {
            return getListWithSort(field);
        }

        return getList();
    }

    public PassengersListResponse getBlockedPassengersList() {
        List<PassengerResponse> responseList = passengerRepository.findAllByIsBlockedIsTrue()
                .stream()
                .map(this::toDto)
                .toList();
        return PassengersListResponse.builder()
                .passengers(responseList)
                .size(responseList.size())
                .total(responseList.size())
                .build();
    }

    public void updateRating(RatingUpdateDto dto) {
        Passenger passenger = getEntityById(dto.getUserId());
        passenger.setRating(dto.getRating());
        passengerRepository.save(passenger);
    }
}
