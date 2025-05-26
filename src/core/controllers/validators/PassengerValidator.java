package core.controllers.validators;

import core.models.Flight;
import core.models.Passenger;
import core.models.storage.FlightStorage;
import core.models.storage.PassengerStorage;
import java.time.DateTimeException;
import java.time.LocalDate;

public class PassengerValidator {

    public static long parsePassengerId(String id) {
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

    public static Passenger getRequiredPassenger(String passengerId) {
        Passenger passenger = PassengerStorage.getInstance().get(passengerId);
        if (passenger == null) {
            throw new IllegalArgumentException("Pasajero con el ID seleccionado no encontrado.");
        }
        return passenger;
    }

    public static Flight getRequiredFlight(String flightId) {
        Flight flight = FlightStorage.getInstance().get(flightId);
        if (flight == null) {
            throw new IllegalArgumentException("Vuelo con el ID seleccionado no encontrado.");
        }
        return flight;
    }

    public static void validateStringField(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío.");
        }
    }

    public static LocalDate parseBirthDate(String yearStr, String monthStr, String dayStr) {
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

    public static int parsePhoneCode(String countryPhoneCode) {
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

    public static long parsePhoneNumber(String phone) {
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