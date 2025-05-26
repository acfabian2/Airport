package core.services;

import core.models.Flight;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List; // Use List interface for better flexibility

/**
 * Provides a utility method to order a list of Flight objects.
 */
public class FlightOrderer {

    /**
     * Orders a list of Flight objects by their departure date in ascending order.
     * This method creates a new list to hold the sorted elements, leaving the original list unchanged.
     *
     * @param originalList The list of Flight objects to be ordered.
     * @return A new ArrayList containing the flights sorted by departure date.
     */
    public static ArrayList<Flight> order(List<Flight> originalList) {
        // Create a new ArrayList to avoid modifying the original list.
        // If modification of the original list is acceptable, this copy can be skipped.
        ArrayList<Flight> orderedList = new ArrayList<>(originalList);

        // Use Collections.sort with a lambda expression for custom comparison.
        // This is significantly simpler and often more performant than manual insertion sort.
        Collections.sort(orderedList, Comparator.comparing(Flight::getDepartureDate));

        return orderedList;
    }
}