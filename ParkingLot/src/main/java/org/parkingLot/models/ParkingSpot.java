package org.parkingLot.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ParkingSpot {
    private int spotId;
    private SPOT_STATUS status;
    private VEHICLE_TYPE type;
    private Vehicle parkedVehicle;

    public ParkingSpot(int spotId, SPOT_STATUS status, VEHICLE_TYPE type) {
        this.spotId = spotId;
        this.status = status;
        this.type = type;
    }

    public synchronized boolean assignVehicle(Vehicle vehicle) {
        if(this.status != SPOT_STATUS.AVAILABLE || this.getType() != vehicle.getVehicleType())
            return false;

        this.parkedVehicle = vehicle;
        this.status = SPOT_STATUS.OCCUPIED;

        return true;
    }

    public synchronized void removeVehicle() {
        this.parkedVehicle = null;
        this.status = SPOT_STATUS.AVAILABLE;
    }
}
