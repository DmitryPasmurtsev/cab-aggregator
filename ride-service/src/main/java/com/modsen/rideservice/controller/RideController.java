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
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/rides")
@RequiredArgsConstructor
@Tag(name = "Контроллер для работы с поездками")
public class RideController {

    private final RideService rideService;

    @GetMapping("/history/{id}")
    @Operation(
            summary = "Получение истории поездок для пассажира"
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
            summary = "Получение поездки по id"
    )
    public RideResponse getRideById(@PathVariable Long id) {
        return rideService.getById(id);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление поездки"
    )
    public StringResponse deleteRide(@PathVariable Long id) {
        rideService.deleteRide(id);
        return new StringResponse("Ride with id={" + id + "} has been removed");
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(
            summary = "Создание поездки"
    )
    public RideResponse addRide(@RequestBody @Valid RideCreationRequest dto) {
        return rideService.addRide(dto);
    }

    @PatchMapping("/{id}/driver/reject")
    @Operation(
            summary = "Отклонение поездки водителем"
    )
    public StringResponse rejectRideForDriver(@PathVariable Long id,@RequestBody @Valid UserIdRequest dto) {
        rideService.driverRejectRide(id, dto);
        return new StringResponse("Ride with id={" + id + "} has been successfully rejected");
    }

    @PatchMapping("/{id}/passenger/reject")
    @Operation(
            summary = "Отклонение поездки пассажиром"
    )
    public StringResponse rejectRideForPassenger(@PathVariable Long id,@RequestBody @Valid UserIdRequest dto) {
        rideService.passengerRejectRide(id, dto);
        return new StringResponse("Ride with id={" + id + "} has been successfully rejected");
    }

    @PatchMapping("/{id}/driver/start")
    @Operation(
            summary = "Поездка начата"
    )
    public StringResponse startRideForDriver(@PathVariable Long id,@RequestBody @Valid UserIdRequest dto) {
        rideService.startRide(id, dto);
        return new StringResponse("Ride with id={" + id + "} has been started");
    }

    @PatchMapping("/{id}/passenger/finish")
    @Operation(
            summary = "Завершение поездки для пассажира"
    )
    public StringResponse finishRideForPassenger(@PathVariable Long id,@RequestBody @Valid PassengerFinishRequest dto) {
        rideService.finishRide(id, dto);
        return new StringResponse("Ride with id={" + id + "} has been successfully finished");
    }

    @PatchMapping("/{id}/driver/finish")
    @Operation(
            summary = "Завершение поездки для водителя"
    )
    public StringResponse finishRideForDriver(@PathVariable Long id,@RequestBody @Valid DriverFinishRequest dto) {
        rideService.finishRide(id, dto);
        return new StringResponse("Ride with id={" + id + "} has been successfully finished");
    }


}
