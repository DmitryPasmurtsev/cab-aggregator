package com.modsen.driverservice.controller;

import com.modsen.driverservice.dto.DriverCreationRequest;
import com.modsen.driverservice.dto.DriverResponse;
import com.modsen.driverservice.dto.RatingResponse;
import com.modsen.driverservice.dto.StringResponse;
import com.modsen.driverservice.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/drivers")
@Tag(name = "Контроллер для работы с водителями")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(
            summary = "Получение всех водителей"
    )
    public ResponseEntity<?> getAllDrivers(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) String field) {
        if (offset != null && page != null && field != null)
            return ResponseEntity.ok(driverService.getAllDrivers(offset, page, field));
        else if (offset != null && page != null)
            return ResponseEntity.ok(driverService.getAllDrivers(offset, page));
        else if (field != null) return ResponseEntity.ok(driverService.getAllDrivers(field));
        else return ResponseEntity.ok(driverService.getAllDrivers());
    }

    @GetMapping("/available")
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(
            summary = "Получение доступных водителей"
    )
    public ResponseEntity<?> getAvailableDrivers(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) String field) {
        if (offset != null && page != null && field != null)
            return ResponseEntity.ok(driverService.getAvailableDrivers(offset, page, field));
        else if (offset != null && page != null)
            return ResponseEntity.ok(driverService.getAvailableDrivers(offset, page));
        else if (field != null) return ResponseEntity.ok(driverService.getAvailableDrivers(field));
        else return ResponseEntity.ok(driverService.getAvailableDrivers());
    }

    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(
            summary = "Получение водителя по id"
    )
    public DriverResponse getDriverById(@PathVariable Long id) {
        System.err.println(driverService.getDTOById(id).isAvailable());
        return driverService.getDTOById(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(
            summary = "Добавление водителя"
    )
    public StringResponse addDriver(@RequestBody @Valid DriverCreationRequest driverDTO) {
        return new StringResponse("Driver with id={" + driverService.addDriver(driverDTO).getId() + "} has been added");
    }

    @PatchMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(
            summary = "Редактирование информации о водителе"
    )
    public StringResponse updateDriver(@PathVariable Long id, @Valid @RequestBody DriverCreationRequest driverDTO) {
        driverService.updateDriver(id, driverDTO);
        return new StringResponse("Driver with id={" + id + "} has been changed");
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
