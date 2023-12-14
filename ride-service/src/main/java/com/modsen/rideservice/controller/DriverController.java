package com.modsen.rideservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/drivers")
@RequiredArgsConstructor
@Tag(name = "Контроллер для работы с поездками")
public class RideController {

    private final DriverService driverService;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(
            summary = "Получение всех водителей"
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
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(
            summary = "Получение доступных водителей"
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
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(
            summary = "Получение водителя по id"
    )
    public DriverResponse getDriverById(@PathVariable Long id) {
        System.err.println(driverService.getById(id).isAvailable());
        return driverService.getById(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(
            summary = "Добавление водителя"
    )
    public DriverResponse addDriver(@RequestBody @Valid DriverCreationRequest driverDTO) {
        return driverService.addDriver(driverDTO);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(
            summary = "Редактирование информации о водителе"
    )
    public DriverResponse updateDriver(@PathVariable Long id, @Valid @RequestBody DriverCreationRequest driverDTO) {
        return driverService.updateDriver(id, driverDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(
            summary = "Удаление водителя"
    )
    public StringResponse deleteDriver(@PathVariable Long id) {
        driverService.deleteDriver(id);
        return new StringResponse("Driver with id={" + id + "} has been removed");
    }


    @GetMapping("/{id}/rating")
    @Operation(
            summary = "Получение рейтинга водителя"
    )
    @ResponseStatus(value = HttpStatus.OK)
    //этот метод в будущем стоит перенести в микросервис рейтингов
    public RatingResponse getDriverRating(@PathVariable Long id) {
        return new RatingResponse(driverService.getRatingById(id), id);
    }

    @PatchMapping("/{id}/available")
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(
            summary = "Изменение статуса"
    )
    public StringResponse changeAvailability(@PathVariable Long id) {
        return new StringResponse("Availability status of driver with id={" + id + "} has been changed. Now is_available={" + driverService.changeAvailabilityStatus(id) + "}");
    }


}
