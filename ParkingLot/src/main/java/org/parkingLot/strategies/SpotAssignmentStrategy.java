package org.parkingLot.strategies;

import org.parkingLot.models.ParkingSpot;
import org.parkingLot.models.VEHICLE_TYPE;

import java.util.List;
import java.util.Optional;

public interface SpotAssignmentStrategy {
    Optional<ParkingSpot> findSpot(List<ParkingSpot> parkingSpots, VEHICLE_TYPE vehicleType);
}
