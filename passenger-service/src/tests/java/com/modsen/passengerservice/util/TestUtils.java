package com.modsen.passengerservice.util;

import com.modsen.passengerservice.dto.request.PassengerCreationRequest;
import com.modsen.passengerservice.dto.request.RatingUpdateDto;
import com.modsen.passengerservice.dto.response.PassengerResponse;
import com.modsen.passengerservice.dto.response.PassengersListResponse;
import com.modsen.passengerservice.entity.Passenger;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class TestUtils {
    public final long DEFAULT_ID = 1L;
    public final long NEW_ID = 2L;
    public final long THIRD_ID = 3L;
    public final String DEFAULT_NAME = "Name";
    public final String NEW_NAME = "NewName";
    public final String DEFAULT_SURNAME = "Surname";
    public final String NEW_SURNAME = "NewSurname";
    public final String DEFAULT_EMAIL = "111@gmail.com";
    public final String DEFAULT_PHONE = "80291234567";
    public final Double NEW_RATING = 5.0;
    public final String NEW_EMAIL = "222@gmail.com";
    public final String NEW_PHONE = "80291237567";
    public final String UNIQUE_EMAIL = "333@gmail.com";
    public final String UNIQUE_PHONE = "80441237567";
    public final String INVALID_EMAIL = "444";
    public final String INVALID_PHONE = "qwerty";
    public final int VALID_PAGE = 0;
    public final int VALID_SIZE = 10;
    public final int VALID_TOTAL = 2;
    public final int INVALID_PAGE = -1;
    public final int INVALID_SIZE = -1;
    public final String INVALID_ORDER_BY = "xyz";
    public final String VALID_ORDER_BY = "id";
    public final boolean NOT_BLOCKED = false;
    public final boolean BLOCKED = true;

    public final String PASSENGER_NAME_NOT_EMPTY = "validation.passenger.name.notEmpty";
    public final String PASSENGER_SURNAME_NOT_EMPTY = "validation.passenger.surname.notEmpty";
    public final String PASSENGER_PHONE_NOT_EMPTY = "validation.passenger.phone.notEmpty";
    public final String PASSENGER_EMAIL_NOT_EMPTY = "validation.passenger.email.notEmpty";
    public final String PASSENGER_PHONE_NOT_VALID = "validation.passenger.phone.notValid";
    public final String PASSENGER_EMAIL_NOT_VALID = "validation.passenger.email.notValid";
    public final String PASSENGER_ID_NOT_FOUND = "message.passenger.id.notFound";
    public final String PASSENGER_EMAIL_ALREADY_EXISTS = "message.passenger.email.alreadyExists";
    public final String PASSENGER_PHONE_ALREADY_EXISTS = "message.passenger.phone.alreadyExists";
    public final String PASSENGER_IS_BLOCKED = "validation.passenger.isBlocked";
    public final String MESSAGE_FIELD_NAME = "message";

    public final String KAFKA_IMAGE = "confluentinc/cp-kafka:6.2.1";
    public final String POSTGRES_IMAGE = "postgres:15-alpine";


    public Passenger getDefaultPassenger() {
        return Passenger.builder()
                .id(DEFAULT_ID)
                .name(DEFAULT_NAME)
                .surname(DEFAULT_SURNAME)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .isBlocked(NOT_BLOCKED)
                .build();
    }

    public Passenger getSecondPassenger() {
        return Passenger.builder()
                .id(NEW_ID)
                .name(NEW_NAME)
                .surname(NEW_SURNAME)
                .email(NEW_EMAIL)
                .phone(NEW_PHONE)
                .build();
    }

    public PassengerResponse getDefaultPassengerResponse() {
        return PassengerResponse.builder()
                .id(DEFAULT_ID)
                .name(DEFAULT_NAME)
                .surname(DEFAULT_SURNAME)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .build();
    }

    public PassengerResponse getSecondPassengerResponse() {
        return PassengerResponse.builder()
                .id(NEW_ID)
                .name(NEW_NAME)
                .surname(NEW_SURNAME)
                .email(NEW_EMAIL)
                .phone(NEW_PHONE)
                .build();
    }

    public Passenger getNotSavedPassengerEntity() {
        return Passenger.builder()
                .name(DEFAULT_NAME)
                .surname(DEFAULT_SURNAME)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .build();
    }

    public RatingUpdateDto getRatingUpdateDto() {
        return RatingUpdateDto.builder()
                .userId(DEFAULT_ID)
                .rating(NEW_RATING)
                .build();
    }

    public List<Passenger> getEntityList() {
        return new ArrayList<>(Arrays.asList(
                getDefaultPassenger(),
                getSecondPassenger()
        ));
    }

    public Page<Passenger> getEntityPage() {
        return new PageImpl<>(Arrays.asList(
                getDefaultPassenger(),
                getSecondPassenger()),
                Pageable.ofSize(VALID_SIZE),
                VALID_TOTAL
        );
    }

    public List<PassengerResponse> getPassengerResponsesList() {
        return new ArrayList<>(Arrays.asList(
                getDefaultPassengerResponse(),
                getSecondPassengerResponse()
        ));
    }

    public PassengersListResponse getDefaultPassengersListResponse(List<PassengerResponse> passengers) {
        Collections.reverse(passengers);
        return PassengersListResponse.builder()
                .passengers(passengers)
                .size(passengers.size())
                .total(passengers.size())
                .build();
    }

    public PassengersListResponse getPassengersListResponseWithSort(List<PassengerResponse> passengers) {
        return PassengersListResponse.builder()
                .passengers(passengers)
                .size(Math.min(passengers.size(), VALID_SIZE))
                .total(passengers.size())
                .sortedByField(VALID_ORDER_BY)
                .build();
    }

    public PassengersListResponse getPassengersListResponseWithPagination(List<PassengerResponse> passengers) {
        return PassengersListResponse.builder()
                .passengers(passengers)
                .size(Math.min(passengers.size(), VALID_SIZE))
                .total(passengers.size())
                .page(VALID_PAGE)
                .build();
    }

    public PassengersListResponse getPassengersListResponseWithSortAndPagination(List<PassengerResponse> passengers) {
        return PassengersListResponse.builder()
                .passengers(passengers)
                .size(Math.min(passengers.size(), VALID_SIZE))
                .total(passengers.size())
                .page(VALID_PAGE)
                .sortedByField(VALID_ORDER_BY)
                .build();
    }

    public PassengerCreationRequest getDefaultPassengerCreationRequest() {
        return PassengerCreationRequest.builder()
                .name(DEFAULT_NAME)
                .surname(DEFAULT_SURNAME)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .build();
    }

    public PassengerCreationRequest getNotValidPassengerCreationRequest() {
        return PassengerCreationRequest.builder()
                .email(INVALID_EMAIL)
                .phone(INVALID_PHONE)
                .build();
    }

    public PassengerCreationRequest getPassengerRequestWithUniqueData() {
        return PassengerCreationRequest.builder()
                .name(NEW_NAME)
                .surname(NEW_SURNAME)
                .email(UNIQUE_EMAIL)
                .phone(UNIQUE_PHONE)
                .build();
    }

    public PassengerCreationRequest getNotUniqueEmailPassengerRequest() {
        return PassengerCreationRequest.builder()
                .name(NEW_NAME)
                .surname(NEW_SURNAME)
                .email(NEW_EMAIL)
                .phone(UNIQUE_PHONE)
                .build();
    }

    public PassengerCreationRequest getNotUniquePhonePassengerRequest() {
        return PassengerCreationRequest.builder()
                .name(NEW_NAME)
                .surname(NEW_SURNAME)
                .email(UNIQUE_EMAIL)
                .phone(NEW_PHONE)
                .build();
    }

    public PassengerResponse getUpdatedPassengerResponse() {
        return PassengerResponse.builder()
                .id(DEFAULT_ID)
                .name(NEW_NAME)
                .surname(NEW_SURNAME)
                .email(UNIQUE_EMAIL)
                .phone(UNIQUE_PHONE)
                .build();
    }

    public PassengerResponse getThirdPassengerResponse() {
        return PassengerResponse.builder()
                .id(THIRD_ID)
                .name(NEW_NAME)
                .surname(NEW_SURNAME)
                .email(UNIQUE_EMAIL)
                .phone(UNIQUE_PHONE)
                .build();
    }
}
