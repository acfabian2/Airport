package core.controllers;

import core.controllers.responses.Response;
import core.controllers.responses.Status;
import core.models.Flight;
import core.models.Passenger;
import core.models.storage.FlightStorage;
import core.models.storage.PassengerStorage;
import core.models.storage.utils.PassengerDataLoader;
import core.models.storage.utils.LineFileReader;
import core.patterns.observer.UserManager;
import core.services.PassengerOrder;
import core.services.PassengerManager;
import core.services.formatters.PassengerFlightFormatter;
import core.services.formatters.PassengerFormatter;
import core.controllers.validators.PassengerValidator; // Import the new validator
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PassengerController {

    public static Response loadPassengersFromJson(String path) {
        try {
            PassengerDataLoader loader = new PassengerDataLoader(PassengerStorage.getInstance());
            String jsonPassengers = LineFileReader.readFile(path);
            loader.loadFromFile(jsonPassengers);
            return new Response("Pasajeros cargados exitosamente.", Status.OK);
        } catch (Exception e) {
            return new Response("No se pudieron cargar los pasajeros. Intente de nuevo más tarde.", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response getAllPassengers() {
        try {
            ArrayList<Passenger> originalList = PassengerStorage.getInstance().getAll();
            ArrayList<Passenger> orderedList = PassengerOrder.order(originalList);
            return new Response("Pasajeros recuperados exitosamente.", Status.OK, orderedList);
        } catch (Exception e) {
            return new Response("No se pudieron recuperar los pasajeros. Intente de nuevo más tarde.", Status.INTERNAL_SERVER_ERROR, new ArrayList<>());
        }
    }

    public static Response addPassenger(String id, String firstname, String lastname, String year, String month, String day, String countryPhoneCode, String phone, String country) {
        try {
            long longId = PassengerValidator.parsePassengerId(id); // Use validator
            if (PassengerStorage.getInstance().get(id) != null) {
                throw new IllegalArgumentException("Un pasajero ya utiliza ese ID.");
            }

            PassengerValidator.validateStringField(firstname, "El nombre"); // Use validator
            PassengerValidator.validateStringField(lastname, "El apellido"); // Use validator
            PassengerValidator.validateStringField(country, "El país"); // Use validator

            LocalDate birthDate = PassengerValidator.parseBirthDate(year, month, day); // Use validator
            int intPhoneCode = PassengerValidator.parsePhoneCode(countryPhoneCode); // Use validator
            long longPhone = PassengerValidator.parsePhoneNumber(phone); // Use validator

            PassengerStorage.getInstance().add(new Passenger(longId, firstname, lastname, birthDate, intPhoneCode, longPhone, country));
            return new Response("Pasajero creado exitosamente.", Status.CREATED);
        } catch (IllegalArgumentException e) {
            return new Response(e.getMessage(), Status.BAD_REQUEST);
        } catch (Exception e) {
            return new Response("Ocurrió un error inesperado al agregar el pasajero. Por favor, intente de nuevo.", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response updatePassenger(String id, String firstname, String lastname, String year, String month, String day, String countryPhoneCode, String phone, String country) {
        try {
            long longId = PassengerValidator.parsePassengerId(id); // Use validator
            Passenger passenger = PassengerValidator.getRequiredPassenger(id); // Use validator

            PassengerValidator.validateStringField(firstname, "El nombre"); // Use validator
            PassengerValidator.validateStringField(lastname, "El apellido"); // Use validator
            PassengerValidator.validateStringField(country, "El país"); // Use validator

            LocalDate birthDate = PassengerValidator.parseBirthDate(year, month, day); // Use validator
            int intPhoneCode = PassengerValidator.parsePhoneCode(countryPhoneCode); // Use validator
            long longPhone = PassengerValidator.parsePhoneNumber(phone); // Use validator

            Passenger updatedPassenger = new Passenger(longId, firstname, lastname, birthDate, intPhoneCode, longPhone, country);
            if (!PassengerStorage.getInstance().update(updatedPassenger)) {
                throw new IllegalStateException("No se pudo actualizar el pasajero en la base de datos.");
            }
            return new Response("Datos del pasajero actualizados exitosamente.", Status.OK);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new Response(e.getMessage(), Status.BAD_REQUEST);
        } catch (Exception e) {
            return new Response("Ocurrió un error inesperado al actualizar el pasajero. Por favor, intente de nuevo.", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response addToFlight(String passengerId, String flightId) {
        try {
            Passenger passenger = PassengerValidator.getRequiredPassenger(passengerId); // Use validator
            Flight flight = PassengerValidator.getRequiredFlight(flightId); // Use validator

            if (flight.getNumPassengers() == flight.getPlane().getMaxCapacity()) {
                throw new IllegalArgumentException("El vuelo está lleno. No se pueden añadir más pasajeros.");
            }

            new PassengerManager().addPassenger(flight, passenger);
            return new Response("Pasajero añadido al vuelo exitosamente.", Status.OK);
        } catch (IllegalArgumentException e) {
            return new Response(e.getMessage(), Status.BAD_REQUEST);
        } catch (Exception e) {
            return new Response("Ocurrió un error inesperado al añadir el pasajero al vuelo. Por favor, intente de nuevo.", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response showPassengerFlights(String passengerId) {
        try {
            Passenger passenger = PassengerValidator.getRequiredPassenger(passengerId); // Use validator
            List<Flight> flights = passenger.getFlights();
            if (flights.isEmpty()) {
                return new Response("El pasajero no tiene vuelos registrados.", Status.OK, new ArrayList<>());
            }
            PassengerFlightFormatter formatter = new PassengerFlightFormatter();
            ArrayList<String[]> data = new ArrayList<>();
            flights.forEach(flight -> data.add(formatter.format(flight)));
            return new Response("Vuelos del pasajero recuperados exitosamente.", Status.OK, data);
        } catch (IllegalArgumentException e) {
            return new Response(e.getMessage(), Status.BAD_REQUEST);
        } catch (Exception e) {
            return new Response("No se pudieron recuperar los vuelos del pasajero. Intente de nuevo más tarde.", Status.INTERNAL_SERVER_ERROR, new ArrayList<>());
        }
    }

    public static Response getPassengersWithFormat() {
        try {
            List<Passenger> passengers = (List<Passenger>) getAllPassengers().getObject();
            PassengerFormatter formatter = new PassengerFormatter();
            ArrayList<String[]> data = new ArrayList<>();
            passengers.forEach(passenger -> data.add(formatter.format(passenger)));
            return new Response("Pasajeros recuperados exitosamente.", Status.OK, data);
        } catch (Exception e) {
            return new Response("No se pudieron recuperar los pasajeros con formato. Intente de nuevo más tarde.", Status.INTERNAL_SERVER_ERROR, new ArrayList<>());
        }
    }

    public static Response changeUser(String id) {
        try {
            if (id.equals("Select User")) {
                throw new IllegalArgumentException("Por favor, seleccione un usuario primero.");
            }
            Passenger passenger = PassengerValidator.getRequiredPassenger(id); // Use validator
            UserManager.getInstance().setCurrentUser(passenger);
            return new Response("Usuario cambiado exitosamente.", Status.OK);
        } catch (IllegalArgumentException e) {
            return new Response(e.getMessage(), Status.BAD_REQUEST);
        } catch (Exception e) {
            return new Response("Ocurrió un error inesperado al cambiar de usuario. Por favor, intente de nuevo.", Status.INTERNAL_SERVER_ERROR, new ArrayList<>());
        }
    }
}