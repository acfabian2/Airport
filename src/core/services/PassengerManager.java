package core.services;

import core.models.Flight;
import core.models.Passenger;
import core.models.storage.FlightStorage;
import core.models.storage.PassengerStorage;

/**
 * Manages operations related to passengers, such as adding a passenger to a flight.
 */
public class PassengerManager {

    /**
     * Adds a passenger to a specific flight and updates the respective storage systems.
     * This method ensures the bidirectional relationship between a flight and a passenger is maintained.
     *
     * @param flight The Flight to which the passenger is to be added.
     * @param passenger The Passenger to add.
     */
    public void addPassenger(Flight flight, Passenger passenger) {
        // Obtain singleton instances of storage.
        // Consider injecting these dependencies instead of using singletons directly for better testability.
        FlightStorage flightStorage = FlightStorage.getInstance();
        PassengerStorage passengerStorage = PassengerStorage.getInstance();

        // Add passenger to flight's list of passengers
        flight.getPassengers().add(passenger);
        // Add flight to passenger's list of flights
        passenger.getFlights().add(flight);

        // Update the storage for both entities to reflect the changes.
        passengerStorage.update(passenger);
        flightStorage.update(flight);
    }
}