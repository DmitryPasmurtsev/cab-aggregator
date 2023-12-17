package com.modsen.rideservice.entity;

import jakarta.persistence.*;
import lombok.*;
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
