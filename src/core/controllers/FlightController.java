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
import core.models.storage.utils.LineFileReader;
import core.services.FlightCoordinator;
import core.services.FlightOrderer;
import core.services.formatters.FlightFormatter;
import core.controllers.validators.FlightValidator; // Import the new validator
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
            FlightValidator.validateFlightId(id); // Use validator
            if (FlightStorage.getInstance().get(id) != null) {
                throw new IllegalArgumentException("Ya existe un vuelo con ese ID.");
            }

            Plane plane = FlightValidator.getRequiredPlane(planeId); // Use validator
            Location departure = FlightValidator.getRequiredLocation(departureId, "salida"); // Use validator
            Location arrival = FlightValidator.getRequiredLocation(arrivalId, "llegada"); // Use validator

            LocalDateTime departureLocalDate = FlightValidator.parseDepartureDateTime(year, month, day, hour, minutes); // Use validator

            int hoursArrival = FlightValidator.parseTimePart(hoursArrivalStr, "hora de llegada"); // Use validator
            int minutesArrival = FlightValidator.parseTimePart(minutesArrivalStr, "minuto de llegada"); // Use validator
            if (hoursArrival == 0 && minutesArrival == 0) {
                throw new IllegalArgumentException("La duración del vuelo debe ser mayor a 00:00.");
            }

            Location scale = null;
            int hoursScale = 0;
            int minutesScale = 0;
            boolean hasScale = !scaleId.equals("Location");

            if (hasScale) {
                scale = FlightValidator.getRequiredLocation(scaleId, "escala"); // Use validator
                hoursScale = FlightValidator.parseTimePart(hoursScaleStr, "hora de escala"); // Use validator
                minutesScale = FlightValidator.parseTimePart(minutesScaleStr, "minuto de escala"); // Use validator
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
            Flight flight = FlightValidator.getRequiredFlight(flightId); // Use validator

            int intHour = FlightValidator.parseTimePart(hour, "hora"); // Use validator
            int intMinutes = FlightValidator.parseTimePart(minutes, "minuto"); // Use validator

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
}