package com.modsen.rideservice.controller;

import com.modsen.rideservice.dto.request.DriverFinishRequest;
import com.modsen.rideservice.dto.request.PassengerFinishRequest;
import com.modsen.rideservice.dto.request.RideCreationRequest;
import com.modsen.rideservice.dto.request.UserIdRequest;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.dto.response.RidesListResponse;
import com.modsen.rideservice.dto.response.StringResponse;
import com.modsen.rideservice.service.RideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/rides")
@RequiredArgsConstructor
@Tag(name = "Controller for working with rides")
public class RideController {

    private final RideService rideService;

    @GetMapping("/history/{id}")
    @Operation(
            summary = "Retrieving rides history for a passenger"
    )
    public RidesListResponse getAllRidesForPassenger(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) String field,
            @PathVariable Long id) {
        return rideService.getList(id, offset, page, field);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Getting a ride by id"
    )
    public RideResponse getRideById(@PathVariable Long id) {
        return rideService.getById(id);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Removing a ride"
    )
    public StringResponse deleteRide(@PathVariable Long id) {
        return rideService.deleteRide(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(
            summary = "Create a ride"
    )
    public RideResponse addRide(@RequestBody @Valid RideCreationRequest dto) {
        return rideService.addRide(dto);
    }

    @PutMapping("/{id}/driver/reject")
    @Operation(
            summary = "Ride rejection by driver"
    )
    public StringResponse rejectRideForDriver(@PathVariable Long id, @RequestBody @Valid UserIdRequest dto) {
        return rideService.driverRejectRide(id, dto);
    }

    @PutMapping("/{id}/passenger/reject")
    @Operation(
            summary = "Ride rejection by passenger"
    )
    public StringResponse rejectRideForPassenger(@PathVariable Long id, @RequestBody @Valid UserIdRequest dto) {
        return rideService.passengerRejectRide(id, dto);
    }

    @PutMapping("/{id}/driver/start")
    @Operation(
            summary = "Start ride"
    )
    public StringResponse startRideForDriver(@PathVariable Long id, @RequestBody @Valid UserIdRequest dto) {
        return rideService.startRide(id, dto);
    }

    @PutMapping("/{id}/passenger/finish")
    @Operation(
            summary = "Ending a ride for a passenger"
    )
    public StringResponse finishRideForPassenger(@PathVariable Long id, @RequestBody @Valid PassengerFinishRequest dto) {
        return rideService.finishRide(id, dto);
    }

    @PutMapping("/{id}/driver/finish")
    @Operation(
            summary = "Ending the ride for the driver"
    )
    public StringResponse finishRideForDriver(@PathVariable Long id, @RequestBody @Valid DriverFinishRequest dto) {
        return rideService.finishRide(id, dto);
    }
}
