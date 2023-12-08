package com.modsen.passengerservice.controller;

import com.modsen.passengerservice.dto.PassengerCreationRequest;
import com.modsen.passengerservice.dto.PassengerResponse;
import com.modsen.passengerservice.service.PassengerService;
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
    public ResponseEntity<PassengerResponse> getAllPassengers() {
return null;
    }

    @GetMapping("/id")
    public ResponseEntity<PassengerResponse> getPassengerById(@PathVariable Long id) {
        return null;
    }

    @PostMapping
    public ResponseEntity<PassengerResponse> addPassenger(@RequestBody PassengerCreationRequest passengerDTO) {
        return null;
    }

    @PutMapping
    public ResponseEntity<PassengerResponse> updatePassenger(@RequestBody PassengerCreationRequest passengerDTO) {
        return null;
    }


}
