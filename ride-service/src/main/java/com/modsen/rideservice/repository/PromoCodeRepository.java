package com.modsen.rideservice.repository;

import com.modsen.rideservice.entity.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromoCodeRepository extends JpaRepository<PromoCode, String> {
}
