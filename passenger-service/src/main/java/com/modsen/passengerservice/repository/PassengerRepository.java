package com.modsen.passengerservice.repository;

import com.modsen.passengerservice.entity.Passenger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    Optional<Passenger> getByIdAndIsBlockedIsFalse(Long id);

    Optional<Passenger> findPassengerByEmailAndIsBlockedIsFalse(String email);

    Optional<Passenger> findPassengerByPhoneAndIsBlockedIsFalse(String phone);

    List<Passenger> findAllByIsBlockedIsFalse();

    List<Passenger> findAllByIsBlockedIsTrue();

    Page<Passenger> findAllByIsBlockedIsFalse(Pageable pageable);

    List<Passenger> findAllByIsBlockedIsFalse(Sort sort);
}
