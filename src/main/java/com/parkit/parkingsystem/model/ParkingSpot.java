package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

public class ParkingSpot {
    private int number;
    private ParkingType parkingType;
    private boolean isAvailable;

    public ParkingSpot(int number, ParkingType parkingType, boolean isAvailable) {
        this.number = number;
        this.parkingType = parkingType;
        this.isAvailable = isAvailable;
    }

    public int getId() {
        return number;
    }

    public ParkingSpot setId(int number) {
        this.number = number;
        return this;
    }

    public ParkingType getParkingType() {
        return parkingType;
    }

    public ParkingSpot setParkingType(ParkingType parkingType) {
        this.parkingType = parkingType;
        return this;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public ParkingSpot setAvailable(boolean available) {
        isAvailable = available;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSpot that = (ParkingSpot) o;
        return number == that.number;
    }

    @Override
    public int hashCode() {
        return number;
    }
}
