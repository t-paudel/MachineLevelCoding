package org.parkingLot.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Vehicle {
    private String registrationNumber;
    private VEHICLE_TYPE vehicleType;
}
