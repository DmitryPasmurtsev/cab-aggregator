package com.modsen.passengerservice.controller;

import com.modsen.passengerservice.dto.*;
import com.modsen.passengerservice.service.PassengerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/passengers")
public class PassengerController {

    private final PassengerService passengerService;

    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> getAllPassengers(
            @RequestParam (required = false) Integer offset,
            @RequestParam (required = false) Integer page,
            @RequestParam (required = false) String field) {
        if(offset != null && page !=null && field!=null) return ResponseEntity.ok(passengerService.getListWithPaginationAndSort(offset, page, field));
        else if (offset != null && page != null) return  ResponseEntity.ok(passengerService.getListWithPagination(offset, page));
        else if (field !=null) return ResponseEntity.ok(passengerService.getListWithSort(field));
        else return ResponseEntity.ok(passengerService.getList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public PassengerResponse getPassengerById(@PathVariable Long id) {
        return passengerService.getDTOById(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public StringResponse addPassenger(@RequestBody PassengerCreationRequest passengerDTO) {
        return new StringResponse("Добавлен пассажир с id " + passengerService.addPassenger(passengerDTO).getId());
    }

    @PatchMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public StringResponse updatePassenger(@PathVariable Long id, @RequestBody PassengerCreationRequest passengerDTO) {
        passengerService.updatePassenger(id, passengerDTO);
        return new StringResponse("Изменен пассажир с id " + id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public StringResponse deletePassenger(@PathVariable Long id) {
        passengerService.deletePassenger(id);
        return new StringResponse("Удален пассажир с id " + id);
    }


}
