package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.commons.lang.time.DateUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }



    @AfterAll
    private static void tearDown(){

    }

    @Test
    public void testParkingACar() throws Exception{
        // GIVEN
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

        // WHEN
        dataBasePrepareService.clearDataBaseEntries();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        String vehicleRegNumber="ABCDEF";
        Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
        int isAvailable=parkingSpotDAO.getAvailable(ticket.getParkingSpot()); // return 0 if parking slot is busy

        // THEN
        assertEquals(ticket.getVehicleRegNumber(), "ABCDEF");
        assertEquals(isAvailable,0);

    }



    @Test
    public void testParkingLotExit(){
        // GIVEN
        FareCalculatorService fareCalculatorService;
        Ticket ticket=new Ticket();
        fareCalculatorService = new FareCalculatorService();
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);


         // WHEN
        dataBasePrepareService.clearDataBaseEntries();
        fareCalculatorService.calculateFare(ticket);
        ticketDAO.saveTicket(ticket);

        Date outTimeInDB=new Date(ticketDAO.getTicket("ABCDEF").getOutTime().getTime());
        DateFormat shortDateFormat = DateFormat.getDateTimeInstance(
                DateFormat.SHORT,
                DateFormat.SHORT);
        String outTimeInDBFormatted=shortDateFormat.format(outTimeInDB);
        String outTimeTicketFormatted=shortDateFormat.format(outTime);

        //THEN
        assertEquals(ticketDAO.getTicket("ABCDEF").getPrice(), ticket.getPrice());
        assertEquals(outTimeInDBFormatted,outTimeTicketFormatted);
    }



}
