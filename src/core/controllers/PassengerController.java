/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
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
            long longId = parsePassengerId(id);
            if (PassengerStorage.getInstance().get(id) != null) {
                throw new IllegalArgumentException("Un pasajero ya utiliza ese ID.");
            }

            validateStringField(firstname, "El nombre");
            validateStringField(lastname, "El apellido");
            validateStringField(country, "El país");

            LocalDate birthDate = parseBirthDate(year, month, day);
            int intPhoneCode = parsePhoneCode(countryPhoneCode);
            long longPhone = parsePhoneNumber(phone);

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
            long longId = parsePassengerId(id);
            Passenger passenger = getRequiredPassenger(id);

            validateStringField(firstname, "El nombre");
            validateStringField(lastname, "El apellido");
            validateStringField(country, "El país");

            LocalDate birthDate = parseBirthDate(year, month, day);
            int intPhoneCode = parsePhoneCode(countryPhoneCode);
            long longPhone = parsePhoneNumber(phone);

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
            Passenger passenger = getRequiredPassenger(passengerId);
            Flight flight = getRequiredFlight(flightId);

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
            Passenger passenger = getRequiredPassenger(passengerId);
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
            Passenger passenger = getRequiredPassenger(id);
            UserManager.getInstance().setCurrentUser(passenger);
            return new Response("Usuario cambiado exitosamente.", Status.OK);
        } catch (IllegalArgumentException e) {
            return new Response(e.getMessage(), Status.BAD_REQUEST);
        } catch (Exception e) {
            return new Response("Ocurrió un error inesperado al cambiar de usuario. Por favor, intente de nuevo.", Status.INTERNAL_SERVER_ERROR, new ArrayList<>());
        }
    }

    // --- Métodos de validación y parseo auxiliares ---
    private static long parsePassengerId(String id) {
        if (id.isEmpty()) {
            throw new IllegalArgumentException("El ID no puede estar vacío.");
        }
        try {
            long longId = Long.parseLong(id);
            if (longId < 0) {
                throw new IllegalArgumentException("El ID debe ser positivo.");
            }
            if (String.valueOf(longId).length() > 15) {
                throw new IllegalArgumentException("El ID no puede exceder los 15 dígitos.");
            }
            return longId;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El ID debe ser numérico.");
        }
    }

    private static Passenger getRequiredPassenger(String passengerId) {
        Passenger passenger = PassengerStorage.getInstance().get(passengerId);
        if (passenger == null) {
            throw new IllegalArgumentException("Pasajero con el ID seleccionado no encontrado.");
        }
        return passenger;
    }

    private static Flight getRequiredFlight(String flightId) {
        Flight flight = FlightStorage.getInstance().get(flightId);
        if (flight == null) {
            throw new IllegalArgumentException("Vuelo con el ID seleccionado no encontrado.");
        }
        return flight;
    }

    private static void validateStringField(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío.");
        }
    }

    private static LocalDate parseBirthDate(String yearStr, String monthStr, String dayStr) {
        if (yearStr.isEmpty()) {
            throw new IllegalArgumentException("El año no puede estar vacío.");
        }
        if (monthStr.equals("Month")) {
            throw new IllegalArgumentException("Debe elegir un mes antes de continuar.");
        }
        if (dayStr.equals("Day")) {
            throw new IllegalArgumentException("Debe elegir un día antes de continuar.");
        }

        try {
            int year = Integer.parseInt(yearStr);
            int currentYear = LocalDate.now().getYear();
            if (year < 1900 || year > currentYear) {
                throw new IllegalArgumentException("Por favor, introduzca un año de nacimiento válido entre 1900 y " + currentYear + ".");
            }
            int month = Integer.parseInt(monthStr);
            int day = Integer.parseInt(dayStr);
            return LocalDate.of(year, month, day);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El año/mes/día de nacimiento debe ser un número.");
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("La fecha de nacimiento es inválida o no existe.");
        }
    }

    private static int parsePhoneCode(String countryPhoneCode) {
        if (countryPhoneCode.isEmpty()) {
            throw new IllegalArgumentException("El código de país del teléfono no puede estar vacío.");
        }
        try {
            int code = Integer.parseInt(countryPhoneCode);
            if (code < 0) {
                throw new IllegalArgumentException("El código de teléfono debe ser positivo.");
            }
            if (String.valueOf(code).length() > 3) {
                throw new IllegalArgumentException("El código de teléfono no puede exceder los 3 dígitos.");
            }
            return code;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El código de teléfono debe ser numérico.");
        }
    }

    private static long parsePhoneNumber(String phone) {
        if (phone.isEmpty()) {
            throw new IllegalArgumentException("El teléfono no puede estar vacío.");
        }
        try {
            long phoneNumber = Long.parseLong(phone);
            if (phoneNumber < 0) {
                throw new IllegalArgumentException("El teléfono debe ser positivo.");
            }
            if (String.valueOf(phoneNumber).length() > 11) {
                throw new IllegalArgumentException("El teléfono no puede exceder los 11 dígitos.");
            }
            return phoneNumber;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El teléfono debe ser numérico.");
        }
    }
}