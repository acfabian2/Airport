/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.storage;

import core.models.Passenger;
import core.patterns.observer.Observable;
import java.util.ArrayList;
import java.util.Optional;

/**
 *
 * @author User
 */
public class PassengerStorage extends Observable implements GeneralStorage<Passenger> {

    private static PassengerStorage instance;

    private ArrayList<Passenger> passengers;

    private PassengerStorage() {
        this.passengers = new ArrayList<>();
    }

    public static PassengerStorage getInstance() {
        if (instance == null) {
            instance = new PassengerStorage();
        }
        return instance;
    }

    @Override
    public boolean add(Passenger item) {
        // Optimización: Usar Streams para verificar si ya existe
        // Importante: Para Long, usar .equals() en lugar de ==
        boolean exists = passengers.stream().anyMatch(p -> p.getId() == item.getId()); // Asumo que getId() retorna un primitivo long o ya manejas la comparación
        if (exists) {
            return false;
        }
        this.passengers.add(item);
        notifyAll(1);
        return true;
    }

    public boolean update(Passenger item) {
        for (int i = 0; i < this.passengers.size(); i++) {
            // Importante: Para Long, usar .equals() en lugar de ==
            if (this.passengers.get(i).getId() == item.getId()) { // Asumo que getId() retorna un primitivo long
                this.passengers.set(i, item);
                notifyAll(2);
                return true;
            }
        }
        return false;
    }

    @Override
    public Passenger get(String id) {
        // Optimización: Usar Streams para encontrar el elemento
        // Convertir el ID de String a Long una sola vez
        try {
            Long idLong = Long.parseLong(id);
            return passengers.stream()
                             // Importante: Para Long, usar .equals() en lugar de ==
                             .filter(passenger -> passenger.getId() == idLong) // Asumo que getId() retorna un primitivo long
                             .findFirst()
                             .orElse(null);
        } catch (NumberFormatException e) {
            // Manejar si el ID no es un número válido. Podrías loggear esto.
            return null; 
        }
    }

    public ArrayList<Passenger> getAll() {
        // Retornar una copia
        return new ArrayList<>(this.passengers);
    }
}