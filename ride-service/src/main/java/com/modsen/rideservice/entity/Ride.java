package com.modsen.rideservice.entity;

import com.modsen.rideservice.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Table(name = "rides")
@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    @Column(name = "pick_up")
    String pickUp;
    @Column(name = "date_of_ride")
    Date date;
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