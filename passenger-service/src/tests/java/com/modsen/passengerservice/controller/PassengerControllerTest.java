package com.modsen.passengerservice.controller;

import com.modsen.passengerservice.dto.request.PassengerCreationRequest;
import com.modsen.passengerservice.dto.response.PassengerResponse;
import com.modsen.passengerservice.dto.response.PassengersListResponse;
import com.modsen.passengerservice.exceptions.response.ExceptionResponse;
import com.modsen.passengerservice.repository.PassengerRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Locale;

import static com.modsen.passengerservice.util.TestUtils.BLOCKED;
import static com.modsen.passengerservice.util.TestUtils.DEFAULT_ID;
import static com.modsen.passengerservice.util.TestUtils.NEW_ID;
import static com.modsen.passengerservice.util.TestUtils.PASSENGER_EMAIL_ALREADY_EXISTS;
import static com.modsen.passengerservice.util.TestUtils.PASSENGER_EMAIL_NOT_VALID;
import static com.modsen.passengerservice.util.TestUtils.PASSENGER_ID_NOT_FOUND;
import static com.modsen.passengerservice.util.TestUtils.PASSENGER_NAME_NOT_EMPTY;
import static com.modsen.passengerservice.util.TestUtils.PASSENGER_PHONE_ALREADY_EXISTS;
import static com.modsen.passengerservice.util.TestUtils.PASSENGER_PHONE_NOT_VALID;
import static com.modsen.passengerservice.util.TestUtils.PASSENGER_SURNAME_NOT_EMPTY;
import static com.modsen.passengerservice.util.TestUtils.getDefaultPassenger;
import static com.modsen.passengerservice.util.TestUtils.getDefaultPassengerRequestForUpdate;
import static com.modsen.passengerservice.util.TestUtils.getDefaultPassengerResponse;
import static com.modsen.passengerservice.util.TestUtils.getDefaultPassengersListResponse;
import static com.modsen.passengerservice.util.TestUtils.getNotUniqueEmailPassengerRequest;
import static com.modsen.passengerservice.util.TestUtils.getNotUniquePhonePassengerRequest;
import static com.modsen.passengerservice.util.TestUtils.getNotValidPassengerCreationRequest;
import static com.modsen.passengerservice.util.TestUtils.getPassengerResponsesList;
import static com.modsen.passengerservice.util.TestUtils.getSecondPassenger;
import static com.modsen.passengerservice.util.TestUtils.getSecondPassengerCreationRequest;
import static com.modsen.passengerservice.util.TestUtils.getSecondPassengerResponse;
import static com.modsen.passengerservice.util.TestUtils.getUpdatedPassengerResponse;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PassengerControllerTest {

    @LocalServerPort
    private Integer port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    private final PassengerRepository passengerRepository;
    private final Locale locale;
    private final MessageSource messageSource;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port + "/api/v1/passengers";
    }

    @Test
    @Order(1)
    void getPassengerById_shouldReturnPassengerResponse_whenPassengerExists() {
        passengerRepository.save(getDefaultPassenger());
        PassengerResponse expected = getDefaultPassengerResponse();

        PassengerResponse actual = get("/" + DEFAULT_ID)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(PassengerResponse.class);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Order(2)
    void getPassengerById_shouldReturnExceptionResponse_whenPassengerNotFound() {
        ExceptionResponse actual = get("/" + NEW_ID)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .body()
                .as(ExceptionResponse.class);

        Assertions.assertEquals(1, actual.getErrorMessage().size());
    }

    @Test
    @Order(2)
    void addPassenger_shouldReturnExceptionResponse_whenRequestNotValid() {
        PassengerCreationRequest creationRequest = getNotValidPassengerCreationRequest();

        ExceptionResponse actual = given()
                .request()
                .contentType(ContentType.JSON)
                .body(creationRequest)
                .when()
                .post()
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
                .map(err -> err.get("message"))
                .toList();
        Assertions.assertEquals(4, actualErrors.size());
        Assertions.assertTrue(actualErrors.contains(expectedNameError));
        Assertions.assertTrue(actualErrors.contains(expectedSurnameError));
        Assertions.assertTrue(actualErrors.contains(expectedPhoneError));
        Assertions.assertTrue(actualErrors.contains(expectedEmailError));
    }

    @Test
    @Order(3)
    void addPassenger_shouldReturnPassengerResponse_whenPassengerUnique() {
        PassengerResponse expected = getSecondPassengerResponse();
        PassengerCreationRequest creationRequest = getSecondPassengerCreationRequest();

        PassengerResponse actual = given()
                .request()
                .contentType(ContentType.JSON)
                .body(creationRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .body()
                .as(PassengerResponse.class);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Order(4)
    void addPassenger_shouldReturnExceptionResponse_whenEmailNotUnique() {
        PassengerCreationRequest creationRequest = getNotUniqueEmailPassengerRequest();

        ExceptionResponse actual = given()
                .request()
                .contentType(ContentType.JSON)
                .body(creationRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .body()
                .as(ExceptionResponse.class);

        String expectedError = messageSource.getMessage(PASSENGER_EMAIL_ALREADY_EXISTS, null, locale);

        Assertions.assertEquals(1, actual.getErrorMessage().size());
        Assertions.assertEquals(expectedError, actual.getErrorMessage().get(0).get("message"));
    }

    @Test
    @Order(5)
    void addPassenger_shouldReturnExceptionResponse_whenPhoneNotUnique() {
        PassengerCreationRequest creationRequest = getNotUniquePhonePassengerRequest();

        ExceptionResponse actual = given()
                .request()
                .contentType(ContentType.JSON)
                .body(creationRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .body()
                .as(ExceptionResponse.class);

        String expectedError = messageSource.getMessage(PASSENGER_PHONE_ALREADY_EXISTS, null, locale);

        Assertions.assertEquals(1, actual.getErrorMessage().size());
        Assertions.assertEquals(expectedError, actual.getErrorMessage().get(0).get("message"));
    }

    @Test
    @Order(5)
    void getAllPassengers() {
        passengerRepository.save(getSecondPassenger());

        PassengersListResponse expected = getDefaultPassengersListResponse(getPassengerResponsesList());

        PassengersListResponse actual = get()
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(PassengersListResponse.class);

        Assertions.assertEquals(expected, actual);
        Assertions.assertArrayEquals(expected.getPassengers().toArray(), actual.getPassengers().toArray());
    }

    @Test
    @Order(6)
    void blockPassenger_whenPassengerExists() {
        post("/" + DEFAULT_ID + "/block")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());

        Boolean actualStatus = passengerRepository.findById(DEFAULT_ID).get().isBlocked();
        Assertions.assertEquals(BLOCKED, actualStatus);
    }

    @Test
    @Order(7)
    void blockPassenger_shouldReturnExceptionResponse_whenPassengerNotFound() {
        ExceptionResponse actual = post("/" + DEFAULT_ID + "/block")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .body()
                .as(ExceptionResponse.class);

        String expectedError = messageSource.getMessage(PASSENGER_ID_NOT_FOUND, null, locale);

        Assertions.assertEquals(expectedError, actual.getErrorMessage().get(0).get("message"));
    }

    @Test
    @Order(8)
    void getBlockedPassengers_shouldReturnPassengersListResponse() {
        PassengerResponse passengerResponse = getDefaultPassengerResponse();
        passengerResponse.setBlocked(BLOCKED);
        List<PassengerResponse> passengerResponses = List.of(
                passengerResponse
        );
        PassengersListResponse expected = getDefaultPassengersListResponse(passengerResponses);

        PassengersListResponse actual = get("/blocked")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(PassengersListResponse.class);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Order(9)
    void updatePassenger_shouldReturnExceptionResponse_whenPassengerNotFound() {
        PassengerCreationRequest request = getDefaultPassengerRequestForUpdate();

        ExceptionResponse actual = given()
                .request()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/" + DEFAULT_ID)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .body()
                .as(ExceptionResponse.class);

        String expectedError = messageSource.getMessage(PASSENGER_ID_NOT_FOUND, null, locale);

        Assertions.assertEquals(expectedError, actual.getErrorMessage().get(0).get("message"));
    }

    @Test
    @Order(10)
    void updatePassenger_shouldReturnPassengerResponse_whenPassengerExistsAndDataIsUnique() {
        PassengerResponse expected = getUpdatedPassengerResponse();
        PassengerCreationRequest request = getDefaultPassengerRequestForUpdate();

        passengerRepository.save(getDefaultPassenger());
        PassengerResponse actual = given()
                .request()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/" + DEFAULT_ID)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(PassengerResponse.class);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Order(11)
    void updatePassenger_shouldReturnExceptionResponse_whenEmailIsNotUnique() {
        PassengerCreationRequest request = getNotUniqueEmailPassengerRequest();

        ExceptionResponse actual = given()
                .request()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/" + DEFAULT_ID)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .body()
                .as(ExceptionResponse.class);

        String expectedError = messageSource.getMessage(PASSENGER_EMAIL_ALREADY_EXISTS, null, locale);

        Assertions.assertEquals(expectedError, actual.getErrorMessage().get(0).get("message"));
    }


    @Test
    @Order(11)
    void updatePassenger_shouldReturnExceptionResponse_whenPhoneIsNotUnique() {

        PassengerCreationRequest request = getNotUniquePhonePassengerRequest();

        ExceptionResponse actual = given()
                .request()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/" + DEFAULT_ID)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .body()
                .as(ExceptionResponse.class);

        String expectedError = messageSource.getMessage(PASSENGER_PHONE_ALREADY_EXISTS, null, locale);

        Assertions.assertEquals(expectedError, actual.getErrorMessage().get(0).get("message"));
    }
}