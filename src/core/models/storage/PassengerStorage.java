package core.models.storage;



import core.models.Passenger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PassengerStorage {

    private static final List<Passenger> passengers = new ArrayList<>();

    public static boolean addPassenger(Passenger p) {
        if (getPassengerById(p.getId()) != null) {
            return false;
        }
        passengers.add(p);
        return true;
    }

    // Buscar por ID
    public static Passenger getPassengerById(long id) {
        for (Passenger p : passengers) {
            if (p.getId() == id) {
                return new Passenger(p);
            }
        }
        return null;
    }

    public static boolean updatePassenger(Passenger updated) {
        for (int i = 0; i < passengers.size(); i++) {
            if (passengers.get(i).getId() == updated.getId()) {
                passengers.set(i, updated);
                return true;
            }
        }
        return false;
    }

    public static List<Passenger> getAllSorted() {
        return passengers.stream()
                .sorted(Comparator.comparingLong(Passenger::getId))
                .map(Passenger::new)
                .toList();
    }

    public static List<Passenger> getAllRaw() {
        return new ArrayList<>(passengers);
    }

    public static void clearAll() {
        passengers.clear();
    }
}
