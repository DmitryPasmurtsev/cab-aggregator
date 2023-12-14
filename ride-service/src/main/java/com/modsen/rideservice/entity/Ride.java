package com.modsen.rideservice.entity;

import com.modsen.rideservice.enums.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Table(name = "rides")
@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    @Column(name = "pick_up")
    String pickUp;
    @Column(name = "destination")
    String destination;
    @Column(name = "passenger_id")
    Long passengerId;
    @Column(name = "driver_id")
    Long driverId;
    @Column(name = "cost")
    Double cost;
    @Column(name = "status")
    Status status;
    @OneToOne(mappedBy = "ride", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    Rating rating;
}
