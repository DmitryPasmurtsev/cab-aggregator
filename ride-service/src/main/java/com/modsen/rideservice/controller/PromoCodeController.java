package com.modsen.rideservice.controller;

import com.modsen.rideservice.dto.request.PromoCodeDTO;
import com.modsen.rideservice.dto.request.PromoCodeUpdateRequest;
import com.modsen.rideservice.dto.response.PromoCodesListResponse;
import com.modsen.rideservice.dto.response.StringResponse;
import com.modsen.rideservice.service.PromoCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/promo")
@RequiredArgsConstructor
@Tag(name = "Controller for working with promotional codes")
public class PromoCodeController {

    private final PromoCodeService promoCodeService;

    @GetMapping
    @Operation(
            summary = "Getting all promotional codes"
    )
    public PromoCodesListResponse getAllPromoCodes(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) String field) {
        if (offset != null && page != null && field != null)
            return promoCodeService.getList(offset, page, field);
        else if (offset != null && page != null)
            return promoCodeService.getList(offset, page);
        else if (field != null) return promoCodeService.getList(field);
        else return promoCodeService.getList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Creating a promotional code"
    )
    public PromoCodeDTO createPromo(@RequestBody @Valid PromoCodeDTO dto) {
        return promoCodeService.create(dto);
    }

    @PatchMapping("/{name}")
    @Operation(
            summary = "Changing the promotional code"
    )
    public PromoCodeDTO updatePromo(@PathVariable String name, @RequestBody @Valid PromoCodeUpdateRequest dto) {
        return promoCodeService.update(name, dto);
    }

    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Removing a promotional code"
    )
    public void deletePromo(@PathVariable String name) {
        promoCodeService.delete(name);
    }
}
