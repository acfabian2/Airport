/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.airport.model;

import core.airport.model.Flight;
import java.util.ArrayList;

/**
 *
 * @author edangulo
 */
public class Plane {

    private final String id;
    private String brand;
    private String model;
    private final int maxCapacity;
    private String airline;
    private ArrayList<Flight> flights;

    public Plane(String id, String brand, String model, int maxCapacity, String airline) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.maxCapacity = maxCapacity;
        this.airline = airline;
        this.flights = new ArrayList<>();
    }
    
    // Constructor de copia (Prototype)
    public Plane(Plane other) {
        this.id = other.id;
        this.brand = other.brand;
        this.model = other.model;
        this.maxCapacity = other.maxCapacity;
        this.airline = other.airline;
        this.flights = new ArrayList<>(other.flights); // copia superficial
    }

    @Override
    public String toString() {
        return id + " - " + model + " (" + brand + ") - " + airline;
    }

    public void addFlight(Flight flight) {
        this.flights.add(flight);
    }

    public String getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public String getAirline() {
        return airline;
    }

    public ArrayList<Flight> getFlights() {
        return flights;
    }

    public int getNumFlights() {
        return flights.size();
    }

}
