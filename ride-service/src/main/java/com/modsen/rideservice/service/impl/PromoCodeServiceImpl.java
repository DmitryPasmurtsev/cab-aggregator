package com.modsen.rideservice.service.impl;

import com.modsen.rideservice.dto.request.PromoCodeDto;
import com.modsen.rideservice.dto.request.PromoCodeUpdateRequest;
import com.modsen.rideservice.dto.response.PromoCodesListResponse;
import com.modsen.rideservice.entity.PromoCode;
import com.modsen.rideservice.exceptions.NotCreatedException;
import com.modsen.rideservice.exceptions.NotFoundException;
import com.modsen.rideservice.repository.PromoCodeRepository;
import com.modsen.rideservice.service.PromoCodeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromoCodeServiceImpl implements PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;
    private final ModelMapper modelMapper;


    private PromoCodeDto toDto(PromoCode promoCode) {
        return modelMapper.map(promoCode, PromoCodeDto.class);
    }

    private PromoCode toModel(PromoCodeDto dto) {
        return modelMapper.map(dto, PromoCode.class);
    }

    public void delete(String name) {
        checkExistence(name);

        promoCodeRepository.deleteById(name);
    }

    public PromoCodeDto create(PromoCodeDto dto) {
        checkConstraints(dto);

        PromoCode promoCode = toModel(dto);
        promoCodeRepository.save(promoCode);
        return dto;
    }

    private void checkConstraints(PromoCodeDto dto) {
        if (promoCodeRepository.existsById(dto.getName())) {
            throw new NotCreatedException("name", "message.promo-code.alreadyExists");
        }
    }

    public PromoCodeDto update(String name, PromoCodeUpdateRequest dto) {
        PromoCode promoCode = getEntityById(name);
        promoCode.setCoefficient(dto.getCoefficient());
        return toDto(promoCodeRepository.save(promoCode));
    }

    private PromoCode getEntityById(String name) {
        return promoCodeRepository.findById(name)
                .orElseThrow(() -> new NotFoundException("name", "message.promo-code.notFound"));
    }

    public PromoCodesListResponse getList(Integer offset, Integer page, String field) {
        if (offset != null && page != null && field != null) {
            return getAllWithPaginationAndSorting(offset, page, field);
        } else if (offset != null && page != null) {
            return getAllWithPagination(offset, page);
        } else if (field != null) {
            return getAllWithSorting(field);
        }

        return getAll();
    }

    private PromoCodesListResponse getAll() {
        List<PromoCodeDto> promoCodes = promoCodeRepository.findAll().stream()
                .map(this::toDto)
                .toList();
        return PromoCodesListResponse.builder()
                .codes(promoCodes)
                .size(promoCodes.size())
                .total(promoCodes.size())
                .build();
    }

    private PromoCodesListResponse getAllWithPaginationAndSorting(Integer offset, Integer page, String field) {
        Page<PromoCodeDto> promoCodes = promoCodeRepository.findAll(PageRequest.of(page, offset).withSort(Sort.by(field)))
                .map(this::toDto);
        return PromoCodesListResponse.builder()
                .codes(promoCodes.getContent())
                .size(promoCodes.getContent().size())
                .page(promoCodes.getPageable().getPageNumber())
                .total((int) promoCodes.getTotalElements())
                .sortedByField(field)
                .build();
    }

    private PromoCodesListResponse getAllWithPagination(Integer offset, Integer page) {
        Page<PromoCodeDto> promoCodes = promoCodeRepository.findAll(PageRequest.of(page, offset))
                .map(this::toDto);
        return PromoCodesListResponse.builder()
                .codes(promoCodes.getContent())
                .size(promoCodes.getContent().size())
                .page(promoCodes.getPageable().getPageNumber())
                .total((int) promoCodes.getTotalElements())
                .build();
    }

    public PromoCodesListResponse getAllWithSorting(String field) {
        List<PromoCodeDto> promoCodes = promoCodeRepository.findAll(Sort.by(field)).stream()
                .map(this::toDto)
                .toList();
        return PromoCodesListResponse.builder()
                .codes(promoCodes)
                .size(promoCodes.size())
                .total(promoCodes.size())
                .sortedByField(field)
                .build();
    }

    public PromoCodeDto getById(String name) {
        return toDto(getEntityById(name));
    }

    private void checkExistence(String name) {
        if (!promoCodeRepository.existsById(name)) {
            throw new NotFoundException("name", "message.promo-code.notFound");
        }
    }
}
