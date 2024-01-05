package com.modsen.rideservice.service;

import com.modsen.rideservice.dto.request.PromoCodeDto;
import com.modsen.rideservice.dto.request.PromoCodeUpdateRequest;
import com.modsen.rideservice.dto.response.PromoCodesListResponse;

public interface PromoCodeService {
    void delete(String name);

    PromoCodeDto create(PromoCodeDto dto);

    PromoCodeDto getById(String name);

    PromoCodeDto update(String name, PromoCodeUpdateRequest dto);

    PromoCodesListResponse getList(Integer offset, Integer page, String field);
}
