package org.parkingLot;

import org.parkingLot.models.*;
import org.parkingLot.strategies.FirstAvailableStrategy;
import org.parkingLot.strategies.HourlyPricingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class App 
{
    public static void main( String[] args ) throws ExecutionException, InterruptedException {
        System.out.println( "Welcome to Parking System!" );

        ParkingLot parkingLot = getParkingLot();
        System.out.println("\nAvailable slots in Parking...");
        parkingLot.getSpots().forEach(System.out::println);

        List<Vehicle> vehicleList = getVehicles();
        System.out.println("\nVehicles in parking queue...");
        vehicleList.forEach(System.out::println);

        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<Future<ParkingTicket>> ticketFutures = new ArrayList<>();

        System.out.println("\n Simulating Concurrent Parking of Vehicles");
        for(Vehicle v : vehicleList) {
            Callable<ParkingTicket> task = () -> {
                try {
                    ParkingTicket ticket = parkingLot.parkVehicle(v);
                    System.out.printf("[%s] SUCCESS | Vehicle: %s | Ticket: %s |" +
                                    "Spot: %s | Time: %s%n",
                            Thread.currentThread().getName(),
                            ticket.getRegistrationNumber(),
                            ticket.getTicketId(),
                            ticket.getSpotId(),
                            ticket.getEntryTime());

                    return ticket;
                } catch (IllegalStateException ex) {
                    System.out.printf("[%s] FAILED | Vehicle: %s | Reason: %s\n",
                            Thread.currentThread().getName(),
                            v.getRegistrationNumber(),
                            ex.getMessage());
                }
                return null;
            };
            ticketFutures.add(executor.submit(task));
        }

        // Collect successful tickets
        List<ParkingTicket> activeTickets = new ArrayList<>();
        for (Future<ParkingTicket> future : ticketFutures) {
            ParkingTicket ticket = future.get();
            if (ticket != null) {
                activeTickets.add(ticket);
            }
        }

        System.out.println("\n--- Simulated vehicles parked for a duration... ---\n");
        Thread.sleep(1500); // Simulate time passage

        System.out.println("=== SIMULATING VEHICLE EXITS ===");

        activeTickets.stream().forEach(tkt ->
        {
            try {
                Thread.sleep(1000);
                double charges = parkingLot.exitVehicle(tkt.getTicketId());
                System.out.printf("SUCCESS | Vehicle: %s | Ticket: %s | Spot: %s | Time: %s | Charges: %f%n",
                        tkt.getRegistrationNumber(),
                        tkt.getTicketId(),
                        tkt.getSpotId(),
                        tkt.getEntryTime(),
                        charges);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        executor.shutdown();
    }

    private static ParkingLot getParkingLot() {
        ParkingLot parkingLot = ParkingLot.getInstance(new FirstAvailableStrategy(),
                new HourlyPricingStrategy());

        ParkingSpot spot1 = new ParkingSpot(1, SPOT_STATUS.AVAILABLE, VEHICLE_TYPE.BIKE);
        ParkingSpot spot2 = new ParkingSpot(2, SPOT_STATUS.AVAILABLE, VEHICLE_TYPE.BIKE);
        ParkingSpot spot3 = new ParkingSpot(3, SPOT_STATUS.OUT_OF_SERVICE, VEHICLE_TYPE.BIKE);
        ParkingSpot spot4 = new ParkingSpot(4, SPOT_STATUS.AVAILABLE, VEHICLE_TYPE.CAR);
        ParkingSpot spot5 = new ParkingSpot(5, SPOT_STATUS.AVAILABLE, VEHICLE_TYPE.CAR);
        ParkingSpot spot6 = new ParkingSpot(6, SPOT_STATUS.AVAILABLE, VEHICLE_TYPE.CAR);
        ParkingSpot spot7 = new ParkingSpot(7, SPOT_STATUS.OUT_OF_SERVICE, VEHICLE_TYPE.CAR);
        ParkingSpot spot8 = new ParkingSpot(8, SPOT_STATUS.AVAILABLE, VEHICLE_TYPE.TRUCK);
        ParkingSpot spot9 = new ParkingSpot(9, SPOT_STATUS.AVAILABLE, VEHICLE_TYPE.TRUCK);

        parkingLot.addSpot(spot1);
        parkingLot.addSpot(spot2);
        parkingLot.addSpot(spot3);
        parkingLot.addSpot(spot4);
        parkingLot.addSpot(spot5);
        parkingLot.addSpot(spot6);
        parkingLot.addSpot(spot7);
        parkingLot.addSpot(spot8);
        parkingLot.addSpot(spot9);

        return parkingLot;
    }

    private static List<Vehicle> getVehicles() {
        Vehicle v1 = new Vehicle("UK07-B1", VEHICLE_TYPE.BIKE);
        Vehicle v2 = new Vehicle("UK07-B2", VEHICLE_TYPE.BIKE);
        Vehicle v3 = new Vehicle("UK07-B3", VEHICLE_TYPE.BIKE);
        Vehicle v4 = new Vehicle("UK07-C1", VEHICLE_TYPE.CAR);
        Vehicle v5 = new Vehicle("UK07-C2", VEHICLE_TYPE.CAR);
        Vehicle v6 = new Vehicle("UK07-C3", VEHICLE_TYPE.CAR);
        Vehicle v7 = new Vehicle("UK07-T1", VEHICLE_TYPE.TRUCK);
        Vehicle v8 = new Vehicle("UK07-T2", VEHICLE_TYPE.TRUCK);
        Vehicle v9 = new Vehicle("UK07-T3", VEHICLE_TYPE.TRUCK);

        List<Vehicle> vehicleList = new ArrayList<>();
        vehicleList.add(v1);
        vehicleList.add(v2);
        vehicleList.add(v3);
        vehicleList.add(v4);
        vehicleList.add(v5);
        vehicleList.add(v6);
        vehicleList.add(v7);
        vehicleList.add(v8);
        vehicleList.add(v9);

        return vehicleList;
    }
}
