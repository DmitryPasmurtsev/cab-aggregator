package com.modsen.rideservice.controller;

import com.modsen.rideservice.dto.response.RatingResponse;
import com.modsen.rideservice.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/ratings")
@RequiredArgsConstructor
@Tag(name = "Controller for working with ratings")
public class RatingController {

    private final RatingService ratingService;

    @GetMapping("/passenger/{id}")
    @Operation(
            summary = "Obtaining a rating for a passenger"
    )
    public RatingResponse getRatingForPassenger(@PathVariable Long id) {
        return ratingService.getRatingForPassenger(id);
    }

    @GetMapping("/driver/{id}")
    @Operation(
            summary = "Obtaining a rating for a driver"
    )
    public RatingResponse getRatingForDriver(@PathVariable Long id) {
        return ratingService.getRatingForDriver(id);
    }
}
