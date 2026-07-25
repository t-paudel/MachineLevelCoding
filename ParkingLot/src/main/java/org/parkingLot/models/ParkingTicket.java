package org.parkingLot.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ParkingTicket {
    private String ticketId;
    private String registrationNumber;
    private int spotId;
    private LocalDateTime entryTime;

    public ParkingTicket(String ticketId, String registrationNumber, int spotId) {
        this.ticketId = ticketId;
        this.registrationNumber = registrationNumber;
        this.spotId = spotId;
        this.entryTime = LocalDateTime.now();
    }
}
