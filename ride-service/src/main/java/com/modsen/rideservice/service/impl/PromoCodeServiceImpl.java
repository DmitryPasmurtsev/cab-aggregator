package com.modsen.rideservice.service.impl;

import com.modsen.rideservice.dto.request.*;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PromoCodeServiceImpl implements PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;
    private final ModelMapper modelMapper;


    private PromoCodeDTO toDTO(PromoCode promoCode) {
        return modelMapper.map(promoCode, PromoCodeDTO.class);
    }

    private PromoCode toModel(PromoCodeDTO dto) {
        return modelMapper.map(dto, PromoCode.class);
    }

    public void delete(String name) {
        checkExistence(name);
        promoCodeRepository.deleteById(name);
    }

    public PromoCodeDTO create(PromoCodeDTO dto) {
        checkConstraints(dto);
        PromoCode promoCode = toModel(dto);
        promoCodeRepository.save(promoCode);
        return dto;
    }

    private void checkConstraints(PromoCodeDTO dto) {
        if(promoCodeRepository.existsById(dto.getName()))
            throw new NotCreatedException("name", "Promo code "+dto.getName()+ " already exists");
    }

    public PromoCodeDTO update(String name, PromoCodeUpdateRequest dto) {
        PromoCode promoCode = getEntityById(name);
        promoCode.setCoefficient(dto.getCoefficient());
        return toDTO(promoCodeRepository.save(promoCode));
    }

    private PromoCode getEntityById(String name) {
        Optional<PromoCode> promoCode = promoCodeRepository.findById(name);
        if(promoCode.isPresent()) return promoCode.get();
        throw new NotFoundException("name", "Promo code "+name+" not found");
    }

    public PromoCodesListResponse getList() {
        List<PromoCodeDTO> promoCodes = promoCodeRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
        return PromoCodesListResponse.builder()
                .codes(promoCodes)
                .size(promoCodes.size())
                .total(promoCodes.size())
                .build();
    }


    public PromoCodesListResponse getList(Integer offset, Integer page, String field) {
        Page<PromoCodeDTO> promoCodes = promoCodeRepository.findAll(PageRequest.of(page, offset).withSort(Sort.by(field)))
                .map(this::toDTO);
        return PromoCodesListResponse.builder()
                .codes(promoCodes.getContent())
                .size(promoCodes.getContent().size())
                .page(promoCodes.getPageable().getPageNumber())
                .total((int) promoCodes.getTotalElements())
                .sortedByField(field)
                .build();
    }

    public PromoCodesListResponse getList(Integer offset, Integer page) {
        Page<PromoCodeDTO> promoCodes = promoCodeRepository.findAll(PageRequest.of(page, offset))
                .map(this::toDTO);
        return PromoCodesListResponse.builder()
                .codes(promoCodes.getContent())
                .size(promoCodes.getContent().size())
                .page(promoCodes.getPageable().getPageNumber())
                .total((int) promoCodes.getTotalElements())
                .build();
    }

    public PromoCodesListResponse getList(String field) {
        List<PromoCodeDTO> promoCodes = promoCodeRepository.findAll(Sort.by(field)).stream()
                .map(this::toDTO)
                .toList();
        return PromoCodesListResponse.builder()
                .codes(promoCodes)
                .size(promoCodes.size())
                .total(promoCodes.size())
                .sortedByField(field)
                .build();
    }

    public PromoCodeDTO getById(String name) {
        return toDTO(getEntityById(name));
    }

    private void checkExistence(String name) {
        if (!promoCodeRepository.existsById(name)) throw new NotFoundException("name", "Promo code " + name+ " not found");
    }
}
