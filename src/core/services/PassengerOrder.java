/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.services;

import core.models.Passenger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Utility class for ordering lists of Passenger objects.
 * @author joelp
 */
public class PassengerOrder {

    /**
     * Orders a list of passengers by their ID in ascending numerical order.
     * Creates a new list to avoid modifying the original list directly.
     * @param originalList The original list of Passenger objects.
     * @return A new ArrayList containing the passengers ordered by ID.
     */
    public static ArrayList<Passenger> order(ArrayList<Passenger> originalList) {
        ArrayList<Passenger> orderedList = new ArrayList<>(originalList); // Create a copy

        // Corrected: Use comparingLong if Passenger.getId() returns a long
        // If Passenger.getId() returns an int, then comparingInt was correct.
        // Assuming the error message means it's a long.
        Collections.sort(orderedList, Comparator.comparingLong(Passenger::getId));

        return orderedList;
    }
}