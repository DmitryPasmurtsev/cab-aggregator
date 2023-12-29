package com.modsen.driverservice.controller;

import com.modsen.driverservice.dto.request.DriverCreationRequest;
import com.modsen.driverservice.dto.response.DriverResponse;
import com.modsen.driverservice.dto.response.DriversListResponse;
import com.modsen.driverservice.dto.response.RatingResponse;
import com.modsen.driverservice.dto.response.StringResponse;
import com.modsen.driverservice.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/drivers")
@RequiredArgsConstructor
@Tag(name = "Controller for working with drivers")
public class DriverController {

    private final DriverService driverService;

    @GetMapping
    @Operation(
            summary = "Getting all drivers"
    )
    public DriversListResponse getAllDrivers(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) String field) {
        if (offset != null && page != null && field != null)
            return driverService.getAllDrivers(offset, page, field);
        else if (offset != null && page != null)
            return driverService.getAllDrivers(offset, page);
        else if (field != null) return driverService.getAllDrivers(field);
        else return driverService.getAllDrivers();
    }

    @GetMapping("/available")
    @Operation(
            summary = "Getting available drivers"
    )
    public DriversListResponse getAvailableDrivers(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) String field) {
        if (offset != null && page != null && field != null)
            return driverService.getAvailableDrivers(offset, page, field);
        else if (offset != null && page != null)
            return driverService.getAvailableDrivers(offset, page);
        else if (field != null) return driverService.getAvailableDrivers(field);
        else return driverService.getAvailableDrivers();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Getting driver by id"
    )
    public DriverResponse getDriverById(@PathVariable Long id) {
        return driverService.getById(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(
            summary = "Adding a driver"
    )
    public DriverResponse addDriver(@RequestBody @Valid DriverCreationRequest driverDTO) {
        return driverService.addDriver(driverDTO);
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Editing driver information"
    )
    public DriverResponse updateDriver(@PathVariable Long id, @Valid @RequestBody DriverCreationRequest driverDTO) {
        return driverService.updateDriver(id, driverDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Removing a driver"
    )
    public StringResponse deleteDriver(@PathVariable Long id) {
        driverService.deleteDriver(id);
        return new StringResponse("Driver with id={" + id + "} has been removed");
    }


    @GetMapping("/{id}/rating")
    @Operation(
            summary = "Getting a driver rating"
    )
    //этот метод в будущем стоит перенести в микросервис рейтингов
    public RatingResponse getDriverRating(@PathVariable Long id) {
        return new RatingResponse(driverService.getRatingById(id), id);
    }

    @GetMapping("/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Change driver status"
    )
    public void changeAvailability(@PathVariable Long id) {
        driverService.changeAvailabilityStatus(id);
    }
}
