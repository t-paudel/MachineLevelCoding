package org.parkingLot.strategies;

import org.parkingLot.models.ParkingSpot;
import org.parkingLot.models.SPOT_STATUS;
import org.parkingLot.models.VEHICLE_TYPE;

import java.util.List;
import java.util.Optional;

public class FirstAvailableStrategy implements SpotAssignmentStrategy {

    @Override
    public Optional<ParkingSpot> findSpot(List<ParkingSpot> parkingSpots, VEHICLE_TYPE vehicleType) {
        return parkingSpots.stream()
                .filter(spot ->
                        spot.getType().equals(vehicleType) &&
                        spot.getStatus().equals(SPOT_STATUS.AVAILABLE))
                .findFirst();
    }
}
