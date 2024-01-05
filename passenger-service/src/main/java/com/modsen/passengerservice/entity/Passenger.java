package com.modsen.passengerservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Table(name = "passengers")
@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    @Column(name = "name")
    String name;
    @Column(name = "surname")
    String surname;
    @Column(name = "phone")
    String phone;
    @Column(name = "email")
    String email;
    @Column(name ="rating", columnDefinition = "numeric(2,1)")
    Double rating;
    @Column(name = "is_blocked")
    boolean isBlocked;
}
