package org.parkingLot;

import lombok.Getter;
import org.parkingLot.models.ParkingSpot;
import org.parkingLot.models.ParkingTicket;
import org.parkingLot.models.Vehicle;
import org.parkingLot.strategies.PricingStrategy;
import org.parkingLot.strategies.SpotAssignmentStrategy;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ParkingLot {
    private static ParkingLot instance;
    @Getter
    private final List<ParkingSpot> spots = new ArrayList<>();
    private final Map<String, ParkingTicket> activeTickets = new ConcurrentHashMap<>();

    private SpotAssignmentStrategy assignmentStrategy;
    private PricingStrategy pricingStrategy;
    private final ReentrantLock gateLock = new ReentrantLock();

    private ParkingLot(SpotAssignmentStrategy assignmentStrategy, PricingStrategy pricingStrategy) {
        this.assignmentStrategy = assignmentStrategy;
        this.pricingStrategy = pricingStrategy;
    }

    public static synchronized ParkingLot getInstance(SpotAssignmentStrategy assignmentStrategy,
                                                      PricingStrategy pricingStrategy) {
        if(instance == null)
            instance = new ParkingLot(assignmentStrategy, pricingStrategy);

        return instance;
    }

    public void addSpot(ParkingSpot spot) {
        spots.add(spot);
    }

    public ParkingTicket parkVehicle(Vehicle vehicle) {
        gateLock.lock();
        try {
            Optional<ParkingSpot> parkingSpot = assignmentStrategy.findSpot(spots, vehicle.getVehicleType());
            if(!parkingSpot.isPresent())
                throw new IllegalStateException("No parking spot available for " + vehicle.getVehicleType());

            ParkingSpot spot = parkingSpot.get();
            if(spot.assignVehicle(vehicle)) {
                String ticketId = "TKT-" + UUID.randomUUID().toString().substring(0, 10);
                ParkingTicket parkingTicket = new ParkingTicket(ticketId,
                        vehicle.getRegistrationNumber(),
                        spot.getSpotId());
                activeTickets.put(ticketId, parkingTicket);

                return parkingTicket;
            }

            throw new IllegalStateException("Failed to assign spot");
        } finally {
            gateLock.unlock();
        }
    }

    public double exitVehicle(String ticketId) {
        gateLock.lock();
        try {
            ParkingTicket ticket = activeTickets.remove(ticketId);
            if(ticket == null)
                throw new IllegalStateException("Invalid or expired ticket");

            ParkingSpot spot = spots.stream()
                    .filter(s -> s.getSpotId() == ticket.getSpotId())
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Spot not found"));

            spot.removeVehicle();

            return pricingStrategy.calculateFee(ticket.getEntryTime(), LocalDateTime.now(), spot.getType());
        } finally {
            gateLock.unlock();
        }
    }
}
