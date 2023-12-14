package com.modsen.rideservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Table(name = "ratings")
@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Rating {
    @Id
    @Column(name = "ride_id")
    Long id;
    @Column(name = "passenger_id")
    Long passengerId;
    @Column(name = "driver_id")
    Long driverId;
    @Column(name = "passenger_rating")
    Integer passengerRating;
    @Column(name = "driver_rating")
    Integer driverRating;
    @OneToOne
    @MapsId
    @JoinColumn(name = "ride_id")
    private Ride ride;
}
