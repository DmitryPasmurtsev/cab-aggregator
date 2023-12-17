package com.modsen.rideservice.controller;


import com.modsen.rideservice.dto.response.RatingResponse;
import com.modsen.rideservice.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/ratings")
@RequiredArgsConstructor
@Tag(name = "Контроллер для работы с рейтингами")
public class RatingController {

    private final RatingService ratingService;

    @GetMapping("/passenger/{id}")
    @Operation(
            summary = "Получение рейтинга для пассажира"
    )
    public RatingResponse getRatingForPassenger(@PathVariable Long id) {
        return ratingService.getRatingForPassenger(id);
    }

    @GetMapping("/driver/{id}")
    @Operation(
            summary = "Получение рейтинга для водителя"
    )
    public RatingResponse getRatingForDriver(@PathVariable Long id) {
        return ratingService.getRatingForDriver(id);
    }
}
