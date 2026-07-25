package org.parkingLot.strategies;

import org.parkingLot.models.VEHICLE_TYPE;

import java.time.LocalDateTime;

public class HourlyPricingStrategy implements PricingStrategy {

    @Override
    public double calculateFee(LocalDateTime entryTime, LocalDateTime exitTime, VEHICLE_TYPE vehicleType) {
        double timeSpent = exitTime.getSecond() - entryTime.getSecond() == 0
                ? 1
                : exitTime.getSecond() - entryTime.getSecond();
        return BASE_RATE * vehicleType.getMultiplier() *
                timeSpent;
    }
}
