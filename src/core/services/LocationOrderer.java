package core.services;

import core.models.Location;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Provides a utility method to order a list of Location objects.
 */
public class LocationOrderer {

    /**
     * Orders a list of Location objects by their airport ID in ascending order.
     * This method creates a new list to hold the sorted elements, leaving the original list unchanged.
     *
     * @param originalList The list of Location objects to be ordered.
     * @return A new ArrayList containing the locations sorted by airport ID.
     */
    public static ArrayList<Location> order(List<Location> originalList) {
        // Create a new ArrayList to avoid modifying the original list.
        // The original `clone()` within the loop implied a desire for distinct objects
        // or to avoid modifying the original list. If `Location.clone()` performs a deep copy
        // and that's intended, the data loading/processing logic might need review.
        // For simple sorting, just copying the references is usually sufficient if the
        // sorting itself doesn't modify the objects' internal state.
        ArrayList<Location> orderedList = new ArrayList<>(originalList);

        // Use Collections.sort with a lambda expression for custom comparison based on airportId.
        Collections.sort(orderedList, Comparator.comparing(Location::getAirportId));

        return orderedList;
    }
}       