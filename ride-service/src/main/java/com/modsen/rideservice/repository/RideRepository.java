package com.modsen.rideservice.repository;

import com.modsen.rideservice.entity.Ride;
import com.modsen.rideservice.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    List<Ride> findAllByPassengerId(Long id);

    List<Ride> findAllByPassengerId(Long id, Sort sort);

    Page<Ride> findAllByPassengerId(Long id, Pageable pageable);

    List<Ride> findAllByDriverIdIsNull();

    List<Ride> findAllByDriverIdAndStatus(Long id, Status status);
}
