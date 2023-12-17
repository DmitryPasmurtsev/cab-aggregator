package com.modsen.rideservice.service;

import com.modsen.rideservice.dto.request.PromoCodeDTO;
import com.modsen.rideservice.dto.request.PromoCodeUpdateRequest;
import com.modsen.rideservice.dto.response.PromoCodesListResponse;
import org.springframework.stereotype.Service;

@Service
public interface PromoCodeService {
    void delete(String name);

    PromoCodeDTO create(PromoCodeDTO dto);

    PromoCodeDTO getById(String name);

    PromoCodeDTO update(String name, PromoCodeUpdateRequest dto);

    PromoCodesListResponse getList(Integer offset, Integer page, String field);

    PromoCodesListResponse getList(Integer offset, Integer page);

    PromoCodesListResponse getList(String field);

    PromoCodesListResponse getList();
}
