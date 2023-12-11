package com.modsen.passengerservice.controller;

import com.modsen.passengerservice.dto.*;
import com.modsen.passengerservice.service.PassengerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/passengers")
@Tag(name = "Контроллер для работы с пассажирами")
public class PassengerController {

    private final PassengerService passengerService;

    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(
            summary = "Получение всех пассажиров"
    )
    public ResponseEntity<?> getAllPassengers(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) String field) {
        if (offset != null && page != null && field != null)
            return ResponseEntity.ok(passengerService.getListWithPaginationAndSort(offset, page, field));
        else if (offset != null && page != null)
            return ResponseEntity.ok(passengerService.getListWithPagination(offset, page));
        else if (field != null) return ResponseEntity.ok(passengerService.getListWithSort(field));
        else return ResponseEntity.ok(passengerService.getList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(
            summary = "Получение пассажира по id"
    )
    public PassengerResponse getPassengerById(@PathVariable Long id) {
        return passengerService.getDTOById(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(
            summary = "Добавление пассажира"
    )
    public StringResponse addPassenger(@RequestBody @Valid PassengerCreationRequest passengerDTO) {
        return new StringResponse("Добавлен пассажир с id " + passengerService.addPassenger(passengerDTO).getId());
    }

    @PatchMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(
            summary = "Редактирование информации о пассажире"
    )
    public StringResponse updatePassenger(@PathVariable Long id, @Valid @RequestBody PassengerCreationRequest passengerDTO) {
        passengerService.updatePassenger(id, passengerDTO);
        return new StringResponse("Изменен пассажир с id " + id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(
            summary = "Удаление пассажира"
    )
    public StringResponse deletePassenger(@PathVariable Long id) {
        passengerService.deletePassenger(id);
        return new StringResponse("Удален пассажир с id " + id);
    }


    @GetMapping("/{id}/rating")
    @Operation(
            summary = "Получение рейтинга пассажира"
    )
    @ResponseStatus(value = HttpStatus.OK)
    //этот метод в будущем стоит перенести в микросервис рейтингов
    public RatingResponse getPassengerRating(@PathVariable Long id) {
        return new RatingResponse(passengerService.getRatingById(id), id);
    }


}
