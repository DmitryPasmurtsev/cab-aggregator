package com.modsen.driverservice.repository;

import com.modsen.driverservice.entity.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findPassengerByPhone(String phone);

    List<Driver> findAllByIsAvailableIsTrue();

    Page<Driver> findAllByIsAvailableIsTrue(Pageable pageable);

    List<Driver> findAllByIsAvailableIsTrue(Sort sort);
}
