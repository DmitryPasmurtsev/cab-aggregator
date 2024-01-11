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
import java.util.List;

@UtilityClass
public class TestUtils {
    public static final long DEFAULT_ID = 1L;
    public static final long NEW_ID = 2L;
    public static final String DEFAULT_NAME = "Name";
    public static final String NEW_NAME = "NewName";
    public static final String DEFAULT_SURNAME = "Surname";
    public static final String NEW_SURNAME = "NewSurname";
    public static final String DEFAULT_EMAIL = "111@gmail.com";
    public static final String DEFAULT_PHONE = "80291234567";
    public static final Double NEW_RATING = 5.0;
    public static final String NEW_EMAIL = "222@gmail.com";
    public static final String NEW_PHONE = "80291237567";
    public static final int VALID_PAGE = 0;
    public static final int VALID_SIZE = 10;
    public static final int VALID_TOTAL = 2;
    public static final int INVALID_PAGE = -1;
    public static final int INVALID_SIZE = -1;
    public static final String INVALID_ORDER_BY = "xyz";
    public static final String VALID_ORDER_BY = "id";
    public static final boolean NOT_BLOCKED = false;
    public static final boolean BLOCKED = true;

    public static Passenger getDefaultPassenger() {
        return Passenger.builder()
                .id(DEFAULT_ID)
                .name(DEFAULT_NAME)
                .surname(DEFAULT_SURNAME)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .isBlocked(NOT_BLOCKED)
                .build();
    }

    public static Passenger getSecondPassenger() {
        return Passenger.builder()
                .id(NEW_ID)
                .name(NEW_NAME)
                .surname(NEW_SURNAME)
                .email(NEW_EMAIL)
                .phone(NEW_PHONE)
                .build();
    }

    public static PassengerResponse getDefaultPassengerResponse() {
        return PassengerResponse.builder()
                .id(DEFAULT_ID)
                .name(DEFAULT_NAME)
                .surname(DEFAULT_SURNAME)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .build();
    }

    public static PassengerResponse getSecondPassengerResponse() {
        return PassengerResponse.builder()
                .id(NEW_ID)
                .name(NEW_NAME)
                .surname(NEW_SURNAME)
                .email(NEW_EMAIL)
                .phone(NEW_PHONE)
                .build();
    }

    public static Passenger getNotSavedPassengerEntity() {
        return Passenger.builder()
                .name(DEFAULT_NAME)
                .surname(DEFAULT_SURNAME)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .build();
    }

    public static RatingUpdateDto getRatingUpdateDto() {
        return RatingUpdateDto.builder()
                .userId(DEFAULT_ID)
                .rating(NEW_RATING)
                .build();
    }

    public static List<Passenger> getEntityList() {
        return new ArrayList<>(Arrays.asList(
                getDefaultPassenger(),
                getSecondPassenger()
        ));
    }

    public static Page<Passenger> getEntityPage() {
        return new PageImpl<>(Arrays.asList(
                getDefaultPassenger(),
                getSecondPassenger()),
                Pageable.ofSize(VALID_SIZE),
                VALID_TOTAL
        );
    }

    public static List<PassengerResponse> getPassengerResponsesList() {
        return new ArrayList<>(Arrays.asList(
                getDefaultPassengerResponse(),
                getSecondPassengerResponse()
        ));
    }

    public static PassengersListResponse getDefaultPassengersListResponse(List<PassengerResponse> passengers) {
        return PassengersListResponse.builder()
                .passengers(passengers)
                .size(passengers.size())
                .total(passengers.size())
                .build();
    }

    public static PassengersListResponse getPassengersListResponseWithSort(List<PassengerResponse> passengers) {
        return PassengersListResponse.builder()
                .passengers(passengers)
                .size(passengers.size())
                .total(passengers.size())
                .sortedByField(VALID_ORDER_BY)
                .build();
    }

    public static PassengersListResponse getPassengersListResponseWithPagination(List<PassengerResponse> passengers) {
        return PassengersListResponse.builder()
                .passengers(passengers)
                .size(passengers.size())
                .total(passengers.size())
                .page(VALID_PAGE)
                .build();
    }

    public static PassengersListResponse getPassengersListResponseWithSortAndPagination(List<PassengerResponse> passengers) {
        return PassengersListResponse.builder()
                .passengers(passengers)
                .size(passengers.size())
                .total(passengers.size())
                .page(VALID_PAGE)
                .sortedByField(VALID_ORDER_BY)
                .build();
    }

    public static PassengerCreationRequest getDefaultPassengerCreationRequest() {
        return PassengerCreationRequest.builder()
                .name(DEFAULT_NAME)
                .surname(DEFAULT_SURNAME)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .build();
    }

    public static PassengerCreationRequest getDefaultPassengerRequestForUpdate() {
        return PassengerCreationRequest.builder()
                .name(NEW_NAME)
                .surname(NEW_SURNAME)
                .email(NEW_EMAIL)
                .phone(NEW_PHONE)
                .build();
    }

    public static PassengerResponse getUpdatedPassengerResponse() {
        return PassengerResponse.builder()
                .id(DEFAULT_ID)
                .name(NEW_NAME)
                .surname(NEW_SURNAME)
                .email(NEW_EMAIL)
                .phone(NEW_PHONE)
                .build();
    }
}
