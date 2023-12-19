package com.modsen.rideservice.controller;

import com.modsen.rideservice.dto.request.*;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.dto.response.RidesListResponse;
import com.modsen.rideservice.dto.response.StringResponse;
import com.modsen.rideservice.service.RideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
        if (offset != null && page != null && field != null)
            return rideService.getAllRidesForPassenger(id, offset, page, field);
        else if (offset != null && page != null)
            return rideService.getAllRidesForPassenger(id, offset, page);
        else if (field != null) return rideService.getAllRidesForPassenger(id, field);
        else return rideService.getAllRidesForPassenger(id);
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
        rideService.deleteRide(id);
        return new StringResponse("Ride with id={" + id + "} has been removed");
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(
            summary = "Create a ride"
    )
    public RideResponse addRide(@RequestBody @Valid RideCreationRequest dto) {
        return rideService.addRide(dto);
    }

    @PatchMapping("/{id}/driver/reject")
    @Operation(
            summary = "Ride rejection by driver"
    )
    public StringResponse rejectRideForDriver(@PathVariable Long id, @RequestBody @Valid UserIdRequest dto) {
        rideService.driverRejectRide(id, dto);
        return new StringResponse("Ride with id={" + id + "} has been successfully rejected");
    }

    @PatchMapping("/{id}/passenger/reject")
    @Operation(
            summary = "Ride rejection by passenger"
    )
    public StringResponse rejectRideForPassenger(@PathVariable Long id, @RequestBody @Valid UserIdRequest dto) {
        rideService.passengerRejectRide(id, dto);
        return new StringResponse("Ride with id={" + id + "} has been successfully rejected");
    }

    @PatchMapping("/{id}/driver/start")
    @Operation(
            summary = "Start ride"
    )
    public StringResponse startRideForDriver(@PathVariable Long id, @RequestBody @Valid UserIdRequest dto) {
        rideService.startRide(id, dto);
        return new StringResponse("Ride with id={" + id + "} has been started");
    }

    @PatchMapping("/{id}/passenger/finish")
    @Operation(
            summary = "Ending a ride for a passenger"
    )
    public StringResponse finishRideForPassenger(@PathVariable Long id, @RequestBody @Valid PassengerFinishRequest dto) {
        rideService.finishRide(id, dto);
        return new StringResponse("Ride with id={" + id + "} has been successfully finished");
    }

    @PatchMapping("/{id}/driver/finish")
    @Operation(
            summary = "Ending the ride for the driver"
    )
    public StringResponse finishRideForDriver(@PathVariable Long id, @RequestBody @Valid DriverFinishRequest dto) {
        rideService.finishRide(id, dto);
        return new StringResponse("Ride with id={" + id + "} has been successfully finished");
    }
}
