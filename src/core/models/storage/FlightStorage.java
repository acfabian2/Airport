/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.storage;

import core.models.Flight;
import core.patterns.observer.Observable;
import java.util.ArrayList;
import java.util.Optional; // Importar Optional

/**
 *
 * @author User
 */
public class FlightStorage extends Observable implements GeneralStorage<Flight> {

    private static FlightStorage instance;

    private ArrayList<Flight> flights;

    private FlightStorage() {
        this.flights = new ArrayList<>();
    }

    public static FlightStorage getInstance() {
        if (instance == null) {
            instance = new FlightStorage();
        }
        return instance;
    }

    @Override
    public boolean add(Flight item) {
        // Optimización: Usar Streams para verificar si ya existe
        boolean exists = flights.stream().anyMatch(f -> f.getId().equals(item.getId()));
        if (exists) {
            return false;
        }
        this.flights.add(item);
        notifyAll(1); // Notificar observadores si es necesario
        return true;
    }
    
    public boolean update(Flight item) {
        // Optimización: Usar un bucle for tradicional es eficiente para update por índice
        for (int i = 0; i < this.flights.size(); i++) {
            if (this.flights.get(i).getId().equals(item.getId())) {
                this.flights.set(i, item);
                notifyAll(2); // Notificar observadores si es necesario
                return true;
            }
        }
        return false;
    }

    @Override
    public Flight get(String id) {  
        // Optimización: Usar Streams para encontrar el elemento
        return flights.stream()
                      .filter(flight -> flight.getId().equals(id))
                      .findFirst() // Encuentra la primera coincidencia
                      .orElse(null); // Retorna null si no se encuentra
    }

    public ArrayList<Flight> getAll() {
        // Retornar una copia para evitar modificaciones externas directas a la lista interna
        return new ArrayList<>(this.flights); 
    }
}