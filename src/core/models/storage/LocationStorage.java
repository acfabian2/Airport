/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.storage;

import core.models.Location;
import core.patterns.observer.Observable;
import java.util.ArrayList;
import java.util.Optional;

/**
 *
 * @author User
 */
public class LocationStorage extends Observable implements GeneralStorage<Location> {

    private static LocationStorage instance;

    private ArrayList<Location> locations;

    private LocationStorage() {
        this.locations = new ArrayList<>();
    }

    public static LocationStorage getInstance() {
        if (instance == null) {
            instance = new LocationStorage();
        }
        return instance;
    }

    @Override
    public boolean add(Location item) {
        // Optimización: Usar Streams para verificar si ya existe
        boolean exists = locations.stream().anyMatch(l -> l.getAirportId().equals(item.getAirportId()));
        if (exists) {
            return false;
        }
        this.locations.add(item);
        notifyAll(1);
        return true;
    }

    @Override
    public Location get(String id) {
        // Optimización: Usar Streams para encontrar el elemento
        return locations.stream()
                        .filter(location -> location.getAirportId().equals(id))
                        .findFirst()
                        .orElse(null);
    }

    public ArrayList<Location> getAll() {
        // Retornar una copia
        return new ArrayList<>(this.locations);
    }
}