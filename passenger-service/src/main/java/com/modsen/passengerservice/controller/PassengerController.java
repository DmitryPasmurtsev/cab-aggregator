package com.modsen.passengerservice.controller;

import com.modsen.passengerservice.dto.request.PassengerCreationRequest;
import com.modsen.passengerservice.dto.response.PassengerResponse;
import com.modsen.passengerservice.dto.response.PassengersListResponse;
import com.modsen.passengerservice.service.PassengerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/passengers")
@RequiredArgsConstructor
@Tag(name = "Controller for working with passengers")
public class PassengerController {

    private final PassengerService passengerService;

    @GetMapping
    @Operation(
            summary = "Get all passengers"
    )
    public PassengersListResponse getAllPassengers(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) String field) {
        return passengerService.getPassengersList(offset, page, field);
    }

    @GetMapping("/blocked")
    @Operation(
            summary = "Get blocked passengers"
    )
    public PassengersListResponse getBlockedPassengers() {
        return passengerService.getBlockedPassengersList();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get passenger by id"
    )
    public PassengerResponse getPassengerById(@PathVariable Long id) {
        return passengerService.getById(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(
            summary = "Passenger creation"
    )
    public PassengerResponse addPassenger(@RequestBody @Valid PassengerCreationRequest passengerDTO) {
        return passengerService.addPassenger(passengerDTO);
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Update passenger"
    )
    public PassengerResponse updatePassenger(@PathVariable Long id, @Valid @RequestBody PassengerCreationRequest passengerDTO) {
        return passengerService.updatePassenger(id, passengerDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Block passenger"
    )
    public void deletePassenger(@PathVariable Long id) {
        passengerService.blockPassenger(id);
    }
}
