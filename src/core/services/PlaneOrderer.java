/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.services;

import core.models.Plane;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Utility class for ordering lists of Plane objects.
 * @author joelp
 */
public class PlaneOrderer {

    /**
     * Orders a list of planes by their ID in ascending alphabetical order.
     * Creates a new list to avoid modifying the original list directly.
     * @param originalList The original list of Plane objects.
     * @return A new ArrayList containing the planes ordered by ID.
     */
    public static ArrayList<Plane> orderPlanes(ArrayList<Plane> originalList) {
        ArrayList<Plane> orderedList = new ArrayList<>(originalList); // Create a copy

        // Sort the list using a Comparator that compares plane IDs
        Collections.sort(orderedList, Comparator.comparing(Plane::getId));

        return orderedList;
    }
}