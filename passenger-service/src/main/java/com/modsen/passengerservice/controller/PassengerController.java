package com.modsen.passengerservice.controller;

import com.modsen.passengerservice.dto.request.PassengerCreationRequest;
import com.modsen.passengerservice.dto.response.PassengerResponse;
import com.modsen.passengerservice.dto.response.PassengersListResponse;
import com.modsen.passengerservice.dto.response.RatingResponse;
import com.modsen.passengerservice.dto.response.StringResponse;
import com.modsen.passengerservice.service.PassengerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/passengers")
@RequiredArgsConstructor
@Tag(name = "Контроллер для работы с пассажирами")
public class PassengerController {

    private final PassengerService passengerService;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(
            summary = "Получение всех пассажиров"
    )
    public PassengersListResponse getAllPassengers(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) String field) {
        if (offset != null && page != null && field != null)
            return passengerService.getListWithPaginationAndSort(offset, page, field);
        else if (offset != null && page != null)
            return passengerService.getListWithPagination(offset, page);
        else if (field != null) return passengerService.getListWithSort(field);
        else return passengerService.getList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(
            summary = "Получение пассажира по id"
    )
    public PassengerResponse getPassengerById(@PathVariable Long id) {
        return passengerService.getById(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(
            summary = "Добавление пассажира"
    )
    public PassengerResponse addPassenger(@RequestBody @Valid PassengerCreationRequest passengerDTO) {
        return passengerService.addPassenger(passengerDTO);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(
            summary = "Редактирование информации о пассажире"
    )
    public PassengerResponse updatePassenger(@PathVariable Long id, @Valid @RequestBody PassengerCreationRequest passengerDTO) {
        return passengerService.updatePassenger(id, passengerDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(
            summary = "Удаление пассажира"
    )
    public StringResponse deletePassenger(@PathVariable Long id) {
        passengerService.deletePassenger(id);
        return new StringResponse("Passenger with id={" + id + "} has been removed");
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
