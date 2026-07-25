package org.parkingLot.strategies;

import org.parkingLot.models.VEHICLE_TYPE;

import java.time.LocalDateTime;

public interface PricingStrategy {
    double BASE_RATE = 20;
    double calculateFee(LocalDateTime entryTime, LocalDateTime exitTime, VEHICLE_TYPE vehicleType);
}
