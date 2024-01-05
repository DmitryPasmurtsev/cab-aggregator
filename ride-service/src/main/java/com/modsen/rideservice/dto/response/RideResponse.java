package com.modsen.rideservice.dto.response;

import com.modsen.rideservice.enums.PaymentMethod;
import com.modsen.rideservice.enums.Status;
import com.modsen.rideservice.feign.dto.DriverResponse;
import com.modsen.rideservice.feign.dto.PassengerResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RideResponse {
    Long id;
    String pickUp;
    String destination;
    Date date;
    Status status;
    PaymentMethod paymentMethod;
    Double initialCost;
    Double finalCost;
    PassengerResponse passenger;
    DriverResponse driver;
}

