package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }

    @Test
    @DisplayName("should return 60*Fare per minute when duration of parking is an hour for a car")
    public void calculateFareCar(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), 60*Fare.CAR_RATE_PER_MINUTE);
    }

    @Test
    @DisplayName("should return 60*Fare per minute when duration of parking is an hour for a bike")
    public void calculateFareBike(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), 60*Fare.BIKE_RATE_PER_MINUTE);
    }

    @Test
    @DisplayName("should return an exception when parkingType is null")
    public void calculateFareUnkownType(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        Throwable exception =assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
        assertEquals("Unkown Parking Type", exception.getMessage());
    }

    @Test
    @DisplayName("should return an exception when inTime>outTime")
    public void calculateFareBikeWithFutureInTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        Throwable exception=assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
        assertEquals("Out time provided is incorrect:" + ticket.getOutTime().toString(), exception.getMessage());
    }

    @Test
    @DisplayName("should return 45*Fare per minute when duration of parking is 45 mn for a bike")
    public void calculateFareBikeWithLessThanOneHourParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((45 * Fare.BIKE_RATE_PER_MINUTE), ticket.getPrice() );
    }

    @Test
    @DisplayName("should return 45*Fare per minute when duration of parking is 45 mn for a car")
    public void calculateFareCarWithLessThanOneHourParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (45 * Fare.CAR_RATE_PER_MINUTE) , ticket.getPrice());
    }

    @Test
    @DisplayName("should return 24*1.5 when duration of parking is = 24h")
    public void calculateFareCarWithMoreThanADayParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000) );//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (24 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    @Test
    @DisplayName("should return 25*1.5 when duration of parking is = 25h")
    public void calculateFareCarWithMoreThanMoreThanADayParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  25 * 60 * 60 * 1000) );//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (25 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    @Test
    @DisplayName("should return 0 when duration of parking is <= 30")
    public void calculateFareShouldReturn0WhenDurationLessThan30() {
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  29 * 60 * 1000) );// 29 minutes before end
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (0) , ticket.getPrice());
    }

    @Test
    @DisplayName("should return 5% discount for recurrent car")
    public void calculateFareShouldReturn5PercentDiscountOnTicketPriceIfRecurrentCar() {
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  90 * 60 * 1000) );// 90 minutes before end
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket, true);
        assertEquals( (90 * Fare.CAR_RATE_PER_MINUTE * 0.95) , ticket.getPrice());
    }

    @Test
    @DisplayName("should return 5% discount for recurrent bike")
    public void calculateFareShouldReturn5PercentDiscountOnTicketPriceIfRecurrentBike() {
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  90 * 60 * 1000) );// 90 minutes before end
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket, true);
        assertEquals( (90 * Fare.BIKE_RATE_PER_MINUTE * 0.95) , ticket.getPrice());
    }

    @Test
    @DisplayName("should return an exception when outTime is null")
    public void calculateFareShouldReturnAnExceptionForOutTimeIsNull() {
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = null;
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        Throwable exception = assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
        assertEquals("Out time provided is null:", exception.getMessage());
    }


}
