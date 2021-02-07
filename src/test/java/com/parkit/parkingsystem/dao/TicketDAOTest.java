package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

class TicketDAOTest {
    private TicketDAO ticketDAO = new TicketDAO();

    @Test
    void saveTicketShouldReturnTicket() {
        Ticket ticket = new Ticket()
                .setInTime(new Date())
                .setPrice(10.0)
                .setVehicleRegNumber("ABCDEF")
                .setParkingSpot(new ParkingSpot(1, ParkingType.CAR, true));

        Assertions.assertEquals(ticketDAO.saveTicket(ticket),ticket);
    }

    @Test
    void saveTicketShouldReturnNullForEmptyRegNumber() {
        Ticket ticket = new Ticket()
                .setInTime(new Date())
                .setPrice(10.0)
                .setVehicleRegNumber(null)
                .setParkingSpot(new ParkingSpot(1, ParkingType.CAR, true));

        assertEquals(ticketDAO.saveTicket(ticket),null);

    }

    @Test
    void getTicketShouldReturn1WhenTicketIsABCDEF() {
        Ticket ticket=ticketDAO.getTicket("ABCDEF");
        assertEquals(ticket.getId(),1);
    }

    @Test
    void getTicketShouldReturnNullWhenTicketIsABCDEFG() {
        Ticket ticket=ticketDAO.getTicket("ABCDEFG");
        assertEquals(ticket,null);
    }

    @Test
    void isRecurrentVehicle() {
        boolean result=ticketDAO.isRecurrentVehicle("ABCDEF");
        assertEquals(result,true);
    }

    @Test
    void updateTicket() {
        Ticket ticket = new Ticket()
                .setPrice(11.0)
                .setOutTime(new Date())
                .setId(1);
        assertEquals(ticketDAO.updateTicket(ticket),true);
    }
}