package org.parkingLot.models;

import lombok.Getter;

public enum VEHICLE_TYPE {
    CAR(2.5),
    BIKE(1.0),
    TRUCK(3.0);

    @Getter
    private final double multiplier;

    VEHICLE_TYPE(double multiplier) {
        this.multiplier = multiplier;
    }
}
