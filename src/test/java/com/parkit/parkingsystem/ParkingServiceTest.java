package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;
    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;
    @Mock
    private static FareCalculatorService fareCalculatorService;


    @Test
    @DisplayName("verify method processExitingVehicle should update vehicule and parking spot to avaiable")
    public void processExitingVehicleTestShouldUpdateVehicule() {
        // GIVEN
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
        // WHEN
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();
        // THEN
        verify(parkingSpotDAO, times(1)).updateParking(parkingSpot);
        assertEquals(ticket.getPrice(), 1.5);
        assertTrue(parkingSpot.isAvailable());
    }

    @Test()
    @DisplayName("verify method processExitingVehicle should not update vehicle and parking spot to avaiable")
    public void processExitingVehicleTestShouldNotUpdateVehicule() {
        // GIVEN
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenThrow(new RuntimeException("Impossible de mettre à jour le ticket"));
        // WHEN
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();
        // THEN
        verify(parkingSpotDAO, times(0)).updateParking(any());
        assertEquals(ticket.getPrice(), 1.5);
        assertFalse(parkingSpot.isAvailable());
    }

    @Test
    @DisplayName("verify method processIncomingVehicle should save vehicle ")
    public void processIncomingVehicleTest() {
        // GIVEN
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(new Ticket());
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);
        // WHEN
        parkingService.processIncomingVehicle();
        //THEN
        verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
        assertFalse(parkingSpot.isAvailable());
    }

    @Test
    @DisplayName("should return ParkingType.CAR when vehicle is a car")
    public void getVehicleTypeShouldReturnCARWhenGetVehicleTypeSelectionIsCar() {
        // GIVEN
        when(inputReaderUtil.readSelection()).thenReturn(1);
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        // WHEN

        //THEN
        assertEquals(parkingService.getVehicleType(), ParkingType.CAR);

    }

    @Test
    @DisplayName("should return ParkingType.BIKE when vehicle is a bike")
    public void getVehicleTypeShouldReturnBIKEWhenGetVehicleTypeSelectionIsBike() {
        when(inputReaderUtil.readSelection()).thenReturn(2);
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        assertEquals(parkingService.getVehicleType(), ParkingType.BIKE);

    }

    @Test
    @DisplayName("should return an exception when vehicle is not car or bike")
    public void getVehicleTypeShouldReturnAnExceptionForReadSelectionNot1Or2() {
        when(inputReaderUtil.readSelection()).thenReturn(3);
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> parkingService.getVehicleType());
        assertEquals("Entered input is invalid", exception.getMessage());
    }


}
