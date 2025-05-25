package core.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Flight {

    private final String id;
    private Plane plane;
    private Location origin;
    private Location destination;
    private Location scale; // Puede ser null
    private LocalDateTime departureTime;
    private Duration scaleTime;
    private Duration flightTime;
    private List<Passenger> passengers;

    public Flight(String id, Plane plane, Location origin, Location destination,
                  Location scale, LocalDateTime departureTime,
                  Duration scaleTime, Duration flightTime) {
        this.id = id;
        this.plane = plane;
        this.origin = origin;
        this.destination = destination;
        this.scale = scale;
        this.departureTime = departureTime;
        this.scaleTime = scaleTime;
        this.flightTime = flightTime;
        this.passengers = new ArrayList<>();
    }

    // ✅ Constructor de copia (Prototype)
    public Flight(Flight other) {
        this.id = other.id;
        this.plane = new Plane(other.plane);
        this.origin = new Location(other.origin);
        this.destination = new Location(other.destination);
        this.scale = (other.scale != null) ? new Location(other.scale) : null;
        this.departureTime = other.departureTime;
        this.scaleTime = other.scaleTime;
        this.flightTime = other.flightTime;
        this.passengers = new ArrayList<>(other.passengers); // copia superficial
    }

    // Métodos de gestión de pasajeros
    public void addPassenger(Passenger p) {
        passengers.add(p);
    }

    public int getNumPassengers() {
        return passengers.size();
    }

    // Getters
    public String getId() {
        return id;
    }

    public Plane getPlane() {
        return plane;
    }

    public Location getOrigin() {
        return origin;
    }

    public Location getDestination() {
        return destination;
    }

    public Location getScale() {
        return scale;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public Duration getScaleTime() {
        return scaleTime;
    }

    public Duration getFlightTime() {
        return flightTime;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    // Setters
    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    public void setOrigin(Location origin) {
        this.origin = origin;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public void setScale(Location scale) {
        this.scale = scale;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public void setScaleTime(Duration scaleTime) {
        this.scaleTime = scaleTime;
    }

    public void setFlightTime(Duration flightTime) {
        this.flightTime = flightTime;
    }

    @Override
    public String toString() {
        return id + " | " + origin.getAirportId() + " → " + destination.getAirportId();
    }
}
