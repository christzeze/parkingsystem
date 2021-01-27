package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        //int inHour = ticket.getInTime().getHours();
        Calendar cal=Calendar.getInstance();
        boolean minutes=false;


        Date maDateInHour=ticket.getInTime(); // arrival date

        Long duration1=maDateInHour.getTime(); // number of milliseconds since January 1, 1970, 00:00:00 for arrival date

        Date maDateOutHour=ticket.getOutTime(); //Departure date

        Long duration2=maDateOutHour.getTime(); // number of milliseconds since January 1, 1970, 00:00:00 for departure date


        long duration=(duration2-duration1)/1000/60; // difference between arrival time and departure time in minutes



        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration*Fare.CAR_RATE_PER_MINUTE); //fare for duration (minutes) for a car
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_MINUTE); //fare for duration (minutes) for a bike
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}

