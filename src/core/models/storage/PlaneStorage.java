/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.storage;

import core.models.Plane;
import core.patterns.observer.Observable;
import java.util.ArrayList;
import java.util.Optional;

/**
 *
 * @author User
 */
public class PlaneStorage extends Observable implements GeneralStorage<Plane> {

    private static PlaneStorage instance;

    private ArrayList<Plane> planes;

    private PlaneStorage() {
        this.planes = new ArrayList<>();
    }

    public static PlaneStorage getInstance() {
        if (instance == null) {
            instance = new PlaneStorage();
        }
        return instance;
    }

    @Override
    public boolean add(Plane item) {
        // Optimización: Usar Streams para verificar si ya existe
        boolean exists = planes.stream().anyMatch(p -> p.getId().equals(item.getId()));
        if (exists) {
            return false;
        }
        this.planes.add(item);
        notifyAll(1);
        return true;
    }

    @Override
    public Plane get(String id) {
        // Optimización: Usar Streams para encontrar el elemento
        return planes.stream()
                     .filter(plane -> plane.getId().equals(id))
                     .findFirst()
                     .orElse(null);
    }

    public ArrayList<Plane> getAll() {
        // Retornar una copia
        return new ArrayList<>(this.planes);
    }
}