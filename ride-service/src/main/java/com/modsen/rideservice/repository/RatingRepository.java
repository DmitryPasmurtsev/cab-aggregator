package com.modsen.rideservice.repository;

import com.modsen.rideservice.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findAllByDriverIdAndDriverRatingIsNotNull(Long id);

    List<Rating> findAllByPassengerIdAndPassengerRatingIsNotNull(Long id);
}
