package com.modsen.rideservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserIdRequest {
    @NotNull(message = "{validation.user.id.notEmpty}")
    Long userId;
}
