package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public static final int FREE_DURATION = 30;
    public static final double DISCOUNT = 0.05;

    /**
     * Compute ticket price for non recurrent vehicles
     *
     * @param ticket
     */
    public void calculateFare(Ticket ticket) {
        calculateFare(ticket, false);
    }

    /**
     * Compute ticket price for recurrent and non recurrent vehicles
     *
     * @param ticket
     * @param isRecurrent
     */
    public void calculateFare(Ticket ticket, boolean isRecurrent) {
        if (ticket.getOutTime() == null) {
            throw new NullPointerException("Out time provided is null:");
        }
        if ((ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        if ((ticket.getParkingSpot().getParkingType()==null)) {
            throw new NullPointerException("Unkown Parking Type");
        }

        if (isRecurrent)
            System.out.println("Welcome back! As a recurring user of our parking lot, you'll benefit from a 5% discount.");

        Long arrivalDateTimestamp = ticket.getInTime().getTime();
        Long departureDateTimestamp = ticket.getOutTime().getTime();
        long durationInMinutes = (departureDateTimestamp - arrivalDateTimestamp) / 1000 / 60;
        if (durationInMinutes <= FREE_DURATION) {
            ticket.setPrice(0);
            return;
        }
        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                ticket.setPrice(durationInMinutes * Fare.CAR_RATE_PER_MINUTE * (isRecurrent ? (1 - DISCOUNT) : 1)); //fare for duration (minutes) for a car
                break;
            }
            case BIKE: {
                ticket.setPrice(durationInMinutes * Fare.BIKE_RATE_PER_MINUTE * (isRecurrent ? (1 - DISCOUNT) : 1)); //fare for duration (minutes) for a bike
                break;
            }
            default:
        }
    }
}

