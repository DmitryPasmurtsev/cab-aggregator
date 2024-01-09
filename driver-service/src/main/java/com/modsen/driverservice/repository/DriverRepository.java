package com.modsen.driverservice.repository;

import com.modsen.driverservice.entity.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findDriverByPhoneAndIsBlockedIsFalse(String phone);

    Optional<Driver> findByIdAndIsBlockedIsFalse(Long id);

    List<Driver> findAllByIsBlockedIsTrue();

    List<Driver> findAllByIsBlockedIsFalse();

    Page<Driver> findAllByIsBlockedIsFalse(Pageable pageable);

    List<Driver> findAllByIsBlockedIsFalse(Sort sort);

    List<Driver> findAllByIsAvailableIsTrueAndIsBlockedIsFalse();

    Page<Driver> findAllByIsAvailableIsTrueAndIsBlockedIsFalse(Pageable pageable);

    List<Driver> findAllByIsAvailableIsTrueAndIsBlockedIsFalse(Sort sort);

    Optional<Driver> findFirstByIsAvailableIsTrueAndIsBlockedIsFalse();
}
