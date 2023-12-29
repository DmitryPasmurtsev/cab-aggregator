package com.modsen.rideservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.rideservice.dto.request.DriverFinishRequest;
import com.modsen.rideservice.dto.request.DriverIdDto;
import com.modsen.rideservice.dto.request.DriverRejectRequest;
import com.modsen.rideservice.dto.request.PassengerFinishRequest;
import com.modsen.rideservice.dto.request.RideCreationRequest;
import com.modsen.rideservice.dto.request.UserIdRequest;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.dto.response.RidesListResponse;
import com.modsen.rideservice.dto.response.StringResponse;
import com.modsen.rideservice.entity.Rating;
import com.modsen.rideservice.entity.Ride;
import com.modsen.rideservice.enums.Status;
import com.modsen.rideservice.exceptions.NoAccessException;
import com.modsen.rideservice.exceptions.NotFoundException;
import com.modsen.rideservice.exceptions.WrongStatusException;
import com.modsen.rideservice.repository.RideRepository;
import com.modsen.rideservice.service.PromoCodeService;
import com.modsen.rideservice.service.RideService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {
    private final RideRepository rideRepository;
    private final PromoCodeService promoCodeService;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;
    private final Locale locale;
    private final KafkaTemplate<String, DriverIdDto> kafkaTemplateDriverId;
    private final KafkaTemplate<String, String> kafkaTemplateString;


    private RideResponse toDto(Ride ride) {
        return modelMapper.map(ride, RideResponse.class);
    }

    private Ride toModel(RideCreationRequest dto) {
        return modelMapper.map(dto, Ride.class);
    }

    public StringResponse deleteRide(Long id) {
        checkExistence(id);
        rideRepository.deleteById(id);
        String message = messageSource.getMessage("message.ride.hasBeenRemoved", null, locale);
        return new StringResponse(message);
    }

    public RideResponse getById(Long id) {
        return toDto(getEntityById(id));
    }

    public RideResponse addRide(RideCreationRequest dto) {
        Ride ride = toModel(dto);
        initializeRideFields(ride, dto);

        notifyDriversAboutRideCreation();

        return toDto(rideRepository.save(ride));
    }

    private void notifyDriversAboutRideCreation() {
        kafkaTemplateString.send("new-rides", "New ride created; Need a driver");
    }

    @KafkaListener(
            topics = "available-drivers",
            groupId = "bar"
    )
    void availableDriverListener(@Payload String data) throws JsonProcessingException {
        DriverIdDto dto = new ObjectMapper().readValue(data, DriverIdDto.class);
        System.err.println("Освободился водитель: " +dto.getDriverId());
        setDriverToRide(dto.getDriverId());
    }

    private void setDriverToRide(Long driverId) {
        System.err.println("начинаем поиск поездки без водителя");
        Optional<Ride> rideOptional = rideRepository.findFirstByDriverIdIsNull();
        if (rideOptional.isPresent()) {
            Ride ride = rideOptional.get();
            System.err.println("Найдена поездка с id " + ride.getId());
            ride.setDriverId(driverId);
            ride.setStatus(Status.ACCEPTED);
            System.err.println("Начинаем сохранять поездку с id " + ride.getId());
            rideRepository.save(ride);
            changeDriverAvailabilityStatus(driverId);
        }
    }

    private void initializeRideFields(Ride ride, RideCreationRequest dto) {
        ride.setInitialCost(calculateInitialCost());
        setFinalCost(ride, dto.getPromoCode());
        ride.setDate(new Date());
        ride.setStatus(Status.NOT_ACCEPTED);
    }

    private Double calculateInitialCost() {
        return Math.round(new Random().nextDouble(5, 20) * 100) / 100.0;
    }

    private Double calculateFinalCost(Double initialCost, Double coefficient) {
        return Math.round(initialCost * coefficient * 100.0) / 100.0;
    }

    private void setFinalCost(Ride ride, String promoCodeName) {
        if (!promoCodeName.isEmpty()) {
            Double coefficient = promoCodeService.getById(promoCodeName).getCoefficient();
            ride.setFinalCost(calculateFinalCost(ride.getInitialCost(), coefficient));
        } else {
            ride.setFinalCost(ride.getInitialCost());
        }
    }

    private Ride getEntityById(Long id) {
        Optional<Ride> ride = rideRepository.findById(id);
        if (ride.isPresent()) {
            return ride.get();
        }
        throw new NotFoundException("id", "message.ride.notFound");
    }

    private Long getAvailableDriverId(DriverRejectRequest dto) {
        //здесь будет отправка запроса на сервис водителей для получения свободного
        //временно реализовано рандомное генерирование id водителя
        return new Random().nextLong(1, 10);
    }

    public StringResponse finishRide(Long id, DriverFinishRequest dto) {
        Ride ride = getEntityById(id);

        checkDriverAccess(ride, dto.getDriverId());
        if (ride.getStatus() != Status.STARTED) {
            throw new WrongStatusException("status", "message.ride.notStarted");
        }

        ride.setStatus(Status.FINISHED);
        Rating rating = Rating.builder()
                .ride(ride)
                .passengerRating(dto.getRatingToPassenger())
                .build();
        ride.setRating(rating);
        rideRepository.save(ride);

        changeDriverAvailabilityStatus(dto.getDriverId());

        String message = messageSource.getMessage("message.ride.hasBeenFinished", null, locale);
        return new StringResponse(message);
    }

    private void changeDriverAvailabilityStatus(Long driverId) {
        kafkaTemplateDriverId.send("change-driver-status", new DriverIdDto(driverId));
    }

    public StringResponse finishRide(Long id, PassengerFinishRequest dto) {
        Ride ride = getEntityById(id);

        checkPassengerAccess(ride, dto.getPassengerId());
        if (dto.getRatingToDriver() != null) {
            ride.getRating().setDriverRating(dto.getRatingToDriver());
        }
        rideRepository.save(ride);

        String message = messageSource.getMessage("message.ride.hasBeenFinished", null, locale);
        return new StringResponse(message);
    }

    public StringResponse startRide(Long id, UserIdRequest dto) {
        Ride ride = getEntityById(id);

        checkDriverAccess(ride, dto.getUserId());
        if (ride.getStatus() != Status.ACCEPTED) {
            throw new WrongStatusException("status", "message.ride.wrongStatus");
        }

        ride.setStatus(Status.STARTED);
        rideRepository.save(ride);

        String message = messageSource.getMessage("message.ride.hasBeenStarted", null, locale);
        return new StringResponse(message);
    }

    public StringResponse passengerRejectRide(Long id, UserIdRequest dto) {
        Ride ride = getEntityById(id);

        checkPassengerAccess(ride, dto.getUserId());
        checkPossibilityToReject(ride);

        ride.setStatus(Status.REJECTED);
        rideRepository.save(ride);

        String message = messageSource.getMessage("message.ride.hasBeenRejected", null, locale);
        return new StringResponse(message);
    }

    public StringResponse driverRejectRide(Long id, UserIdRequest dto) {
        Ride ride = getEntityById(id);

        checkDriverAccess(ride, dto.getUserId());
        checkPossibilityToReject(ride);

        ride.setStatus(Status.NOT_ACCEPTED);
        ride.setDriverId(getAvailableDriverId(null));
        ride.setStatus(Status.ACCEPTED);
        rideRepository.save(ride);

        String message = messageSource.getMessage("message.ride.hasBeenRejected", null, locale);
        return new StringResponse(message);
    }

    private void checkPossibilityToReject(Ride ride) {
        if (ride.getStatus().getValue() > Status.ACCEPTED.getValue()) {
            throw new WrongStatusException("status", "message.ride.notRejected");
        }
    }

    public RidesListResponse getList(Long id, Integer offset, Integer page, String field) {
        if (offset != null && page != null && field != null) {
            return getRidesForPassenger(id, offset, page, field);
        } else if (offset != null && page != null) {
            return getRidesForPassenger(id, offset, page);
        } else if (field != null) {
            return getRidesForPassenger(id, field);
        }

        return getRidesForPassenger(id);
    }

    private RidesListResponse getRidesForPassenger(Long id) {
        List<RideResponse> rides = rideRepository.findAllByPassengerId(id).stream()
                .map(this::toDto)
                .toList();
        return RidesListResponse.builder()
                .rides(rides)
                .size(rides.size())
                .total(rides.size())
                .build();
    }

    private RidesListResponse getRidesForPassenger(Long id, Integer offset, Integer page, String field) {
        Page<RideResponse> ridesPage = rideRepository.findAllByPassengerId(id, PageRequest.of(page, offset).withSort(Sort.by(field)))
                .map(this::toDto);
        return RidesListResponse.builder()
                .rides(ridesPage.getContent())
                .size(ridesPage.getContent().size())
                .page(ridesPage.getPageable().getPageNumber())
                .total((int) ridesPage.getTotalElements())
                .sortedByField(field)
                .build();
    }

    private RidesListResponse getRidesForPassenger(Long id, Integer offset, Integer page) {
        Page<RideResponse> ridesPage = rideRepository.findAllByPassengerId(id, PageRequest.of(page, offset))
                .map(this::toDto);
        return RidesListResponse.builder()
                .rides(ridesPage.getContent())
                .size(ridesPage.getContent().size())
                .page(ridesPage.getPageable().getPageNumber())
                .total((int) ridesPage.getTotalElements())
                .build();
    }

    private RidesListResponse getRidesForPassenger(Long id, String field) {
        List<RideResponse> ridesList = rideRepository.findAllByPassengerId(id, Sort.by(field)).stream()
                .map(this::toDto)
                .toList();
        return RidesListResponse.builder()
                .rides(ridesList)
                .size(ridesList.size())
                .total(ridesList.size())
                .sortedByField(field)
                .build();
    }

    private void checkPassengerAccess(Ride ride, Long passengerId) {
        if (!Objects.equals(ride.getPassengerId(), passengerId)) {
            throw new NoAccessException("userId", "message.passenger.noAccessToRide");
        }
    }

    private void checkDriverAccess(Ride ride, Long driverId) {
        if (!Objects.equals(ride.getDriverId(), driverId)) {
            throw new NoAccessException("userId", "message.driver.noAccessToRide");
        }
    }

    private void checkExistence(Long id) {
        if (!rideRepository.existsById(id)) {
            throw new NotFoundException("id", "message.ride.notFound");
        }
    }
}
