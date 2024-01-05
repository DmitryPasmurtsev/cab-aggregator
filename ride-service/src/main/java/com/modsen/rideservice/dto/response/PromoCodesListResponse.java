package com.modsen.rideservice.dto.response;

import com.modsen.rideservice.dto.request.PromoCodeDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromoCodesListResponse {
    Integer page;
    Integer size;
    Integer total;
    String sortedByField;
    List<PromoCodeDto> codes;
}
