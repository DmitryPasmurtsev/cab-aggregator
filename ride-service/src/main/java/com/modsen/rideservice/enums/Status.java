package com.modsen.rideservice.enums;

import lombok.Getter;

@Getter
public enum Status {
    NOT_ACCEPTED(0),
    ACCEPTED(1),
    STARTED(2),
    FINISHED(3),
    REJECTED(4);

    private final int value;
    Status(int value) {
        this.value = value;
    }
}
