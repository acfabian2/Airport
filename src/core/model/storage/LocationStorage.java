package core.model.storage;

import core.model.Location;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LocationStorage {

    private static final List<Location> locations = new ArrayList<>();

    public static boolean addLocation(Location loc) {
        if (getLocationById(loc.getAirportId()) != null) {
            return false;
        }
        locations.add(loc);
        return true;
    }

    public static Location getLocationById(String id) {
        for (Location loc : locations) {
            if (loc.getAirportId().equalsIgnoreCase(id)) {
                return new Location(loc);
            }
        }
        return null;
    }

    public static List<Location> getAllSorted() {
        return locations.stream()
                .sorted(Comparator.comparing(Location::getAirportId))
                .map(Location::new)
                .toList();
    }

    public static List<Location> getAllRaw() {
        return new ArrayList<>(locations);
    }

    public static void clearAll() {
        locations.clear();
    }
}
