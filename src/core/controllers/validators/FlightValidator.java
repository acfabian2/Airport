package core.controllers.validators;

import core.models.Flight;
import core.models.Location;
import core.models.Plane;
import core.models.storage.FlightStorage;
import core.models.storage.LocationStorage;
import core.models.storage.PlaneStorage;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class FlightValidator {

    public static void validateFlightId(String id) {
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

    public static Plane getRequiredPlane(String planeId) {
        Plane plane = PlaneStorage.getInstance().get(planeId);
        if (plane == null) {
            throw new IllegalArgumentException("El avión no existe.");
        }
        return plane;
    }

    public static Location getRequiredLocation(String locationId, String type) {
        Location location = LocationStorage.getInstance().get(locationId);
        if (location == null) {
            throw new IllegalArgumentException("La ubicación de " + type + " no existe.");
        }
        return location;
    }

    public static Flight getRequiredFlight(String flightId) {
        Flight flight = FlightStorage.getInstance().get(flightId);
        if (flight == null) {
            throw new IllegalArgumentException("El vuelo no existe.");
        }
        return flight;
    }

    public static LocalDateTime parseDepartureDateTime(String yearStr, String monthStr, String dayStr, String hourStr, String minutesStr) {
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

    public static int parseYear(String yearStr) {
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

    public static int parseMonth(String monthStr) {
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

    public static int parseDay(String dayStr) {
        if (dayStr.equals("Day")) {
            throw new IllegalArgumentException("Debe elegir un día antes de continuar.");
        }
        try {
            return Integer.parseInt(dayStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El día debe ser un número.");
        }
    }

    public static int parseTimePart(String timePartStr, String partName) {
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