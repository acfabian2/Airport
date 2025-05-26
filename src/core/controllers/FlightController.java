/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.responses.Response;
import core.controllers.responses.Status;
import core.models.Flight;
import core.models.Location;
import core.models.Plane;
import core.models.storage.FlightStorage;
import core.models.storage.LocationStorage;
import core.models.storage.PlaneStorage;
import core.models.storage.utils.FlightDataLoader;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import core.models.storage.utils.LineFileReader;
import core.services.FlightCoordinator;
import core.services.FlightOrderer;
import core.services.formatters.FlightFormatter;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author User
 */
public class FlightController {

    public static Response loadFlightsFromJson(String path) {
        try {
            FlightDataLoader loader = new FlightDataLoader(
                FlightStorage.getInstance(),
                PlaneStorage.getInstance(),
                LocationStorage.getInstance()
            );
            String jsonFlights = LineFileReader.readFile(path);
            loader.loadFromFile(jsonFlights);
            return new Response("Vuelos cargados exitosamente.", Status.OK);
        } catch (Exception e) {
            return new Response("No se pudieron cargar los vuelos. Intente de nuevo más tarde.", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response getAllFlights() {
        try {
            ArrayList<Flight> originalList = FlightStorage.getInstance().getAll();
            ArrayList<Flight> orderedList = FlightOrderer.order(originalList);
            return new Response("Vuelos recuperados exitosamente.", Status.OK, orderedList);
        } catch (Exception e) {
            return new Response("No se pudieron recuperar los vuelos. Intente de nuevo más tarde.", Status.INTERNAL_SERVER_ERROR, new ArrayList<>());
        }
    }

    public static Response addFlight(String id, String planeId, String departureId, String arrivalId,
                                     String year, String month, String day, String hour, String minutes,
                                     String hoursArrivalStr, String minutesArrivalStr,
                                     String scaleId, String hoursScaleStr, String minutesScaleStr) {
        try {
            validateFlightId(id);
            if (FlightStorage.getInstance().get(id) != null) {
                throw new IllegalArgumentException("Ya existe un vuelo con ese ID.");
            }

            Plane plane = getRequiredPlane(planeId);
            Location departure = getRequiredLocation(departureId, "salida");
            Location arrival = getRequiredLocation(arrivalId, "llegada");

            LocalDateTime departureLocalDate = parseDepartureDateTime(year, month, day, hour, minutes);

            int hoursArrival = parseTimePart(hoursArrivalStr, "hora de llegada");
            int minutesArrival = parseTimePart(minutesArrivalStr, "minuto de llegada");
            if (hoursArrival == 0 && minutesArrival == 0) {
                throw new IllegalArgumentException("La duración del vuelo debe ser mayor a 00:00.");
            }

            Location scale = null;
            int hoursScale = 0;
            int minutesScale = 0;
            boolean hasScale = !scaleId.equals("Location");

            if (hasScale) {
                scale = getRequiredLocation(scaleId, "escala");
                hoursScale = parseTimePart(hoursScaleStr, "hora de escala");
                minutesScale = parseTimePart(minutesScaleStr, "minuto de escala");
                if (hoursScale == 0 && minutesScale == 0) {
                    throw new IllegalArgumentException("El tiempo de escala debe ser mayor a 0 si se especifica una escala.");
                }
            } else if (!hoursScaleStr.equals("Hour") || !minutesScaleStr.equals("Minute")) {
                throw new IllegalArgumentException("No puede haber tiempo de escala si no hay ubicación de escala.");
            }

            Flight flight = hasScale
                    ? new Flight(id, plane, departure, scale, arrival, departureLocalDate, hoursArrival, minutesArrival, hoursScale, minutesScale)
                    : new Flight(id, plane, departure, arrival, departureLocalDate, hoursArrival, minutesArrival);

            FlightStorage.getInstance().add(flight);
            return new Response("Vuelo agregado exitosamente.", Status.CREATED);
        } catch (IllegalArgumentException e) {
            return new Response(e.getMessage(), Status.BAD_REQUEST);
        } catch (Exception e) {
            return new Response("Ocurrió un error inesperado al agregar el vuelo. Por favor, intente de nuevo.", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response delayFlight(String flightId, String hour, String minutes) {
        try {
            Flight flight = getRequiredFlight(flightId);

            int intHour = parseTimePart(hour, "hora");
            int intMinutes = parseTimePart(minutes, "minuto");

            new FlightCoordinator().delay(flight, intHour, intMinutes);
            return new Response("El vuelo ha sido retrasado exitosamente.", Status.OK);
        } catch (IllegalArgumentException e) {
            return new Response(e.getMessage(), Status.BAD_REQUEST);
        } catch (Exception e) {
            return new Response("Ocurrió un error inesperado al retrasar el vuelo. Por favor, intente de nuevo.", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response getFlightsWithFormat() {
        try {
            List<Flight> flights = (List<Flight>) getAllFlights().getObject();
            FlightFormatter formatter = new FlightFormatter();
            ArrayList<String[]> data = new ArrayList<>();
            flights.forEach(flight -> data.add(formatter.format(flight)));
            return new Response("Vuelos recuperados exitosamente.", Status.OK, data);
        } catch (Exception e) {
            return new Response("No se pudieron recuperar los vuelos con formato. Intente de nuevo más tarde.", Status.INTERNAL_SERVER_ERROR, new ArrayList<>());
        }
    }

    // --- Métodos de validación y parseo auxiliares ---
    private static void validateFlightId(String id) {
        if (id.length() != 6) {
            throw new IllegalArgumentException("El ID del vuelo debe tener exactamente 6 caracteres (3 letras seguidas de 3 números).");
        }
        if (!id.substring(0, 3).matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("Los primeros 3 caracteres del ID deben ser letras mayúsculas.");
        }
        if (!id.substring(3, 6).matches("\\d{3}")) {
            throw new IllegalArgumentException("Los últimos 3 caracteres del ID deben ser números.");
        }
    }

    private static Plane getRequiredPlane(String planeId) {
        Plane plane = PlaneStorage.getInstance().get(planeId);
        if (plane == null) {
            throw new IllegalArgumentException("El avión no existe.");
        }
        return plane;
    }

    private static Location getRequiredLocation(String locationId, String type) {
        Location location = LocationStorage.getInstance().get(locationId);
        if (location == null) {
            throw new IllegalArgumentException("La ubicación de " + type + " no existe.");
        }
        return location;
    }

    private static Flight getRequiredFlight(String flightId) {
        Flight flight = FlightStorage.getInstance().get(flightId);
        if (flight == null) {
            throw new IllegalArgumentException("El vuelo no existe.");
        }
        return flight;
    }

    private static LocalDateTime parseDepartureDateTime(String yearStr, String monthStr, String dayStr, String hourStr, String minutesStr) {
        int year = parseYear(yearStr);
        int month = parseMonth(monthStr);
        int day = parseDay(dayStr);
        int hour = parseTimePart(hourStr, "hora");
        int minutes = parseTimePart(minutesStr, "minuto");

        try {
            LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minutes);
            if (dateTime.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("La fecha de salida no puede ser del pasado.");
            }
            return dateTime;
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("La fecha de salida es inválida o no existe: " + e.getMessage());
        }
    }

    private static int parseYear(String yearStr) {
        if (yearStr.isEmpty()) {
            throw new IllegalArgumentException("Debe elegir un año antes de continuar.");
        }
        try {
            int year = Integer.parseInt(yearStr);
            int currentYear = LocalDate.now().getYear();
            if (year < currentYear || year > currentYear + 5) {
                throw new IllegalArgumentException("Por favor, introduzca un año válido entre " + currentYear + " y " + (currentYear + 5) + ".");
            }
            return year;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El año debe ser un número.");
        }
    }

    private static int parseMonth(String monthStr) {
        if (monthStr.equals("Month")) {
            throw new IllegalArgumentException("Debe elegir un mes antes de continuar.");
        }
        try {
            int month = Integer.parseInt(monthStr);
            if (month < 1 || month > 12) {
                throw new IllegalArgumentException("El mes debe estar entre 1 y 12.");
            }
            return month;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El mes debe ser un número.");
        }
    }

    private static int parseDay(String dayStr) {
        if (dayStr.equals("Day")) {
            throw new IllegalArgumentException("Debe elegir un día antes de continuar.");
        }
        try {
            return Integer.parseInt(dayStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El día debe ser un número.");
        }
    }

    private static int parseTimePart(String timePartStr, String partName) {
        if (timePartStr.equals("Hour") || timePartStr.equals("Minute")) {
            throw new IllegalArgumentException("Debe elegir una " + partName + " antes de continuar.");
        }
        try {
            return Integer.parseInt(timePartStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La " + partName + " debe ser un número.");
        }
    }
}