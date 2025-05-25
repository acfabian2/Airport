package core.model.storage;

import core.model.Flight;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FlightStorage {

    private static final List<Flight> flights = new ArrayList<>();

    public static boolean addFlight(Flight f) {
        if (getFlightById(f.getId()) != null) {
            return false;
        }
        flights.add(f);
        return true;
    }

    public static Flight getFlightById(String id) {
        for (Flight f : flights) {
            if (f.getId().equalsIgnoreCase(id)) {
                return new Flight(f);
            }
        }
        return null;
    }

    public static List<Flight> getAllSorted() {
        return flights.stream()
                .sorted(Comparator.comparing(Flight::getDepartureTime))
                .map(Flight::new)
                .toList();
    }

    public static List<Flight> getAllRaw() {
        return new ArrayList<>(flights);
    }

    public static List<Flight> getFlightsByPassengerId(long passengerId) {
        return flights.stream()
                .filter(f -> f.getPassengers().stream().anyMatch(p -> p.getId() == passengerId))
                .sorted(Comparator.comparing(Flight::getDepartureTime))
                .map(Flight::new)
                .toList();
    }
    
    public static void clearAll() {
        flights.clear();
    }
}

