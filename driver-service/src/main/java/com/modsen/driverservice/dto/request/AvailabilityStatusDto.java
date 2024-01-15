package com.modsen.driverservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AvailabilityStatusDto {
    @NotNull(message = "Status should not be empty")
    boolean isAvailable;
}
