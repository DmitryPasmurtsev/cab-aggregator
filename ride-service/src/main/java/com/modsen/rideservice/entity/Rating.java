package com.modsen.rideservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Table(name = "ratings")
@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    @Id
    @Column(name = "ride_id")
    Long id;
    @Column(name = "passenger_rating")
    Integer passengerRating;
    @Column(name = "driver_rating")
    Integer driverRating;

    @OneToOne
    @MapsId
    @JoinColumn(name = "ride_id")
    private Ride ride;
}
