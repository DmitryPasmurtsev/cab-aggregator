package com.modsen.rideservice.repository;

import com.modsen.rideservice.entity.Rating;
import com.modsen.rideservice.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Ride> {
    List<Rating> findAllByRideDriverIdAndDriverRatingIsNotNull(Long id);

    List<Rating> findAllByRidePassengerIdAndPassengerRatingIsNotNull(Long id);
}
