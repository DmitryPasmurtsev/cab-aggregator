package com.modsen.passengerservice.repository;

import com.modsen.passengerservice.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    Optional<Passenger> findPassengerByEmail(String email);

    Optional<Passenger> findPassengerByPhone(String phone);
}
