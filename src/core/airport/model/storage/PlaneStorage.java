package core.airport.model.storage;

import core.airport.model.Plane;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlaneStorage {

    private static final List<Plane> planes = new ArrayList<>();

    public static boolean addPlane(Plane p) {
        if (getPlaneById(p.getId()) != null) {
            return false; 
        }
        planes.add(p);
        return true;
    }

    public static Plane getPlaneById(String id) {
        for (Plane p : planes) {
            if (p.getId().equalsIgnoreCase(id)) {
                return new Plane(p);
            }
        }
        return null;
    }

    public static List<Plane> getAllSorted() {
        return planes.stream()
                .sorted(Comparator.comparing(Plane::getId))
                .map(Plane::new)
                .toList();
    }

    public static List<Plane> getAllRaw() {
        return new ArrayList<>(planes);
    }

    public static void clearAll() {
        planes.clear();
    }
}
