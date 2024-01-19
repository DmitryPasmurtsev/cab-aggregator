package com.modsen.passengerservice.integration.controller;

import com.modsen.passengerservice.dto.request.PassengerCreationRequest;
import com.modsen.passengerservice.dto.response.PassengerResponse;
import com.modsen.passengerservice.dto.response.PassengersListResponse;
import com.modsen.passengerservice.entity.Passenger;
import com.modsen.passengerservice.exceptions.response.ExceptionResponse;
import com.modsen.passengerservice.integration.BaseIntegrationTest;
import com.modsen.passengerservice.repository.PassengerRepository;
import io.restassured.RestAssured;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Locale;

import static com.modsen.passengerservice.util.RestAssuredClient.*;
import static com.modsen.passengerservice.util.TestUtils.*;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PassengerControllerTest extends BaseIntegrationTest {

    @LocalServerPort
    private Integer port;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    private final PassengerRepository passengerRepository;
    private final Locale locale;
    private final MessageSource messageSource;

    @BeforeEach
    public void beforeEach() {
        RestAssured.baseURI = "http://localhost:" + port + "/api/v1/passengers";
    }

    @Test
    void getPassengerById_shouldReturnPassengerResponse_whenPassengerExists() {
        passengerRepository.save(getDefaultPassenger());
        PassengerResponse expected = getDefaultPassengerResponse();

        PassengerResponse actual = sendGetByIdRequest(DEFAULT_ID)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(PassengerResponse.class);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getPassengerById_shouldReturnExceptionResponse_whenPassengerNotFound() {
        ExceptionResponse actual = sendGetByIdRequest(THIRD_ID)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .body()
                .as(ExceptionResponse.class);

        Assertions.assertEquals(1, actual.getErrorMessage().size());
    }

    @Test
    void addPassenger_shouldReturnExceptionResponse_whenRequestNotValid() {
        PassengerCreationRequest creationRequest = getNotValidPassengerCreationRequest();

        ExceptionResponse actual = sendCreationRequest(creationRequest)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .body()
                .as(ExceptionResponse.class);

        String expectedNameError = messageSource.getMessage(PASSENGER_NAME_NOT_EMPTY, null, locale);
        String expectedSurnameError = messageSource.getMessage(PASSENGER_SURNAME_NOT_EMPTY, null, locale);
        String expectedEmailError = messageSource.getMessage(PASSENGER_EMAIL_NOT_VALID, null, locale);
        String expectedPhoneError = messageSource.getMessage(PASSENGER_PHONE_NOT_VALID, null, locale);

        List<String> actualErrors = actual.getErrorMessage().stream()
                .map(err -> err.get(MESSAGE_FIELD_NAME))
                .toList();
        Assertions.assertEquals(4, actualErrors.size());
        Assertions.assertTrue(actualErrors.contains(expectedNameError));
        Assertions.assertTrue(actualErrors.contains(expectedSurnameError));
        Assertions.assertTrue(actualErrors.contains(expectedPhoneError));
        Assertions.assertTrue(actualErrors.contains(expectedEmailError));
    }

    @Test
    void addPassenger_shouldReturnPassengerResponse_whenPassengerUnique() {
        passengerRepository.saveAll(List.of(getDefaultPassenger(), getSecondPassenger()));
        PassengerResponse expected = getThirdPassengerResponse();
        PassengerCreationRequest creationRequest = getPassengerRequestWithUniqueData();

        PassengerResponse actual = sendCreationRequest(creationRequest)
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .body()
                .as(PassengerResponse.class);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void addPassenger_shouldReturnExceptionResponse_whenEmailNotUnique() {
        passengerRepository.save(getSecondPassenger());
        PassengerCreationRequest creationRequest = getNotUniqueEmailPassengerRequest();

        ExceptionResponse actual = sendCreationRequest(creationRequest)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .body()
                .as(ExceptionResponse.class);

        String expectedError = messageSource.getMessage(PASSENGER_EMAIL_ALREADY_EXISTS, null, locale);

        Assertions.assertEquals(1, actual.getErrorMessage().size());
        Assertions.assertEquals(expectedError, actual.getErrorMessage().get(0).get(MESSAGE_FIELD_NAME));
    }

    @Test
    void addPassenger_shouldReturnExceptionResponse_whenPhoneNotUnique() {
        passengerRepository.save(getSecondPassenger());
        passengerRepository.deleteById(THIRD_ID);
        PassengerCreationRequest creationRequest = getNotUniquePhonePassengerRequest();

        ExceptionResponse actual = sendCreationRequest(creationRequest)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .body()
                .as(ExceptionResponse.class);

        String expectedError = messageSource.getMessage(PASSENGER_PHONE_ALREADY_EXISTS, null, locale);

        Assertions.assertEquals(1, actual.getErrorMessage().size());
        Assertions.assertEquals(expectedError, actual.getErrorMessage().get(0).get(MESSAGE_FIELD_NAME));
    }

    @Test
    void getAllPassengers() {
        passengerRepository.saveAll(List.of(getDefaultPassenger(), getSecondPassenger()));
        PassengersListResponse expected = getDefaultPassengersListResponse(getPassengerResponsesList());

        PassengersListResponse actual = sendGetAllRequest(null, null, null)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(PassengersListResponse.class);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getAllPassengersWithSorting() {
        passengerRepository.saveAll(List.of(getDefaultPassenger(), getSecondPassenger()));
        PassengersListResponse expected = getPassengersListResponseWithSort(getPassengerResponsesList());

        PassengersListResponse actual = sendGetAllRequest(null, null, VALID_ORDER_BY)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(PassengersListResponse.class);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getAllPassengersWithPagination() {
        passengerRepository.saveAll(List.of(getDefaultPassenger(), getSecondPassenger()));
        PassengersListResponse expected = getPassengersListResponseWithPagination(getPassengerResponsesList());

        PassengersListResponse actual = sendGetAllRequest(VALID_SIZE, VALID_PAGE, null)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(PassengersListResponse.class);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getAllPassengersWithPaginationAndSorting() {
        passengerRepository.deleteById(THIRD_ID);
        passengerRepository.saveAll(List.of(getDefaultPassenger(), getSecondPassenger()));
        PassengersListResponse expected = getPassengersListResponseWithSortAndPagination(getPassengerResponsesList());

        PassengersListResponse actual = sendGetAllRequest(VALID_SIZE, VALID_PAGE, VALID_ORDER_BY)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(PassengersListResponse.class);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void blockPassenger_whenPassengerExists() {
        passengerRepository.save(getDefaultPassenger());

        sendBlockRequest(DEFAULT_ID)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());

        Boolean actualStatus = passengerRepository.findById(DEFAULT_ID).get().isBlocked();
        Assertions.assertEquals(BLOCKED, actualStatus);
    }

    @Test
    void blockPassenger_shouldReturnExceptionResponse_whenPassengerNotFound() {
        ExceptionResponse actual = sendBlockRequest(DEFAULT_ID)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .body()
                .as(ExceptionResponse.class);

        String expectedError = messageSource.getMessage(PASSENGER_ID_NOT_FOUND, null, locale);

        Assertions.assertEquals(expectedError, actual.getErrorMessage().get(0).get(MESSAGE_FIELD_NAME));
    }

    @Test
    void getBlockedPassengers_shouldReturnPassengersListResponse() {
        Passenger passenger = getDefaultPassenger();
        passenger.setBlocked(BLOCKED);
        passengerRepository.save(passenger);
        PassengerResponse passengerResponse = getDefaultPassengerResponse();
        passengerResponse.setBlocked(BLOCKED);
        List<PassengerResponse> passengerResponses = List.of(
                passengerResponse
        );
        PassengersListResponse expected = getDefaultPassengersListResponse(passengerResponses);

        PassengersListResponse actual = sendGetBlockedPassengersRequest()
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(PassengersListResponse.class);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updatePassenger_shouldReturnExceptionResponse_whenPassengerNotFound() {
        PassengerCreationRequest request = getPassengerRequestWithUniqueData();

        ExceptionResponse actual = sendUpdateRequest(THIRD_ID, request)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .body()
                .as(ExceptionResponse.class);

        String expectedError = messageSource.getMessage(PASSENGER_ID_NOT_FOUND, null, locale);

        Assertions.assertEquals(expectedError, actual.getErrorMessage().get(0).get(MESSAGE_FIELD_NAME));
    }

    @Test
    void updatePassenger_shouldReturnPassengerResponse_whenPassengerExistsAndDataIsUnique() {
        PassengerResponse expected = getUpdatedPassengerResponse();
        PassengerCreationRequest request = getPassengerRequestWithUniqueData();

        passengerRepository.save(getDefaultPassenger());
        PassengerResponse actual = sendUpdateRequest(DEFAULT_ID, request)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(PassengerResponse.class);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updatePassenger_shouldReturnExceptionResponse_whenEmailIsNotUnique() {
        passengerRepository.saveAll(List.of(getDefaultPassenger(), getSecondPassenger()));
        PassengerCreationRequest request = getNotUniqueEmailPassengerRequest();

        ExceptionResponse actual = sendUpdateRequest(DEFAULT_ID, request)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .body()
                .as(ExceptionResponse.class);

        String expectedError = messageSource.getMessage(PASSENGER_EMAIL_ALREADY_EXISTS, null, locale);

        Assertions.assertEquals(expectedError, actual.getErrorMessage().get(0).get(MESSAGE_FIELD_NAME));
    }

    @Test
    void updatePassenger_shouldReturnExceptionResponse_whenPhoneIsNotUnique() {
        passengerRepository.saveAll(List.of(getDefaultPassenger(), getSecondPassenger()));
        PassengerCreationRequest request = getNotUniquePhonePassengerRequest();

        ExceptionResponse actual = sendUpdateRequest(DEFAULT_ID, request)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .body()
                .as(ExceptionResponse.class);

        String expectedError = messageSource.getMessage(PASSENGER_PHONE_ALREADY_EXISTS, null, locale);

        Assertions.assertEquals(expectedError, actual.getErrorMessage().get(0).get(MESSAGE_FIELD_NAME));
    }
}