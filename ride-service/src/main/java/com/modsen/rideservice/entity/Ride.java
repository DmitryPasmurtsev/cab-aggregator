package com.modsen.rideservice.entity;

import com.modsen.rideservice.enums.PaymentMethod;
import com.modsen.rideservice.enums.Status;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    @Column(name = "date_of_ride", columnDefinition = "date")
    Date date;
    @Column(name = "destination")
    String destination;
    @Column(name = "passenger_id")
    Long passengerId;
    @Column(name = "driver_id")
    Long driverId;
    @Column(name = "initial_cost")
    Double initialCost;
    @Column(name = "final_cost")
    Double finalCost;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    Status status;
    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod;
    @OneToOne(mappedBy = "ride", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    Rating rating;
}
