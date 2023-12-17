package com.modsen.rideservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Table(name = "promo_codes")
@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromoCode {
    @Id
    @Column(name = "name")
    String name;
    @Column(name = "coefficient")
    Double coefficient;
}
