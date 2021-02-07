package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ParkingSpotDAOTest {

    @Test
    void getAvailable() {
        ParkingSpotDAO parkingSpotDAO=new ParkingSpotDAO();
        TicketDAO ticketDAO=new TicketDAO();
        String vehicleRegNumber="ABCDEF";
        Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
        int isAvailable=parkingSpotDAO.getAvailable(ticket.getParkingSpot()); // return 0 if parking slot is busy
        assertEquals(isAvailable,0);
    }

    @Test
    void getNextAvailableSlot() {
        ParkingSpotDAO parkingSpotDAO=new ParkingSpotDAO();
        ParkingType parkingType=ParkingType.CAR;
        int firstParkingFree=parkingSpotDAO.ParkingFree();
        assertEquals(parkingSpotDAO.getNextAvailableSlot(parkingType),firstParkingFree);
    }

    @Test
    void updateParking() {
        ParkingSpot parkingSpot=new ParkingSpot(1,ParkingType.CAR,false);
        ParkingSpotDAO parkingSpotDAO=new ParkingSpotDAO();
        parkingSpot.setId(2);
        parkingSpot.setAvailable(false);
        assertEquals(parkingSpotDAO.updateParking(parkingSpot),true);
        }
    }
