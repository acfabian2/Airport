package core.controllers.utils;

import core.models.Plane;
import core.models.Passenger;
import core.models.Location;
import core.models.Flight;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Validator {

    public static boolean isValidPassenger(Passenger p) {
        return p.getId() >= 0
                && String.valueOf(p.getId()).length() <= 15
                && p.getCountryPhoneCode() >= 0
                && String.valueOf(p.getCountryPhoneCode()).length() <= 3
                && p.getPhone() >= 0
                && String.valueOf(p.getPhone()).length() <= 11
                && p.getFirstname() != null && !p.getFirstname().isBlank()
                && p.getLastname() != null && !p.getLastname().isBlank()
                && p.getCountry() != null && !p.getCountry().isBlank()
                && p.getBirthDate() != null && !p.getBirthDate().isAfter(LocalDate.now());
    }

    public static boolean isValidPlane(Plane plane) {
        return plane.getId().matches("^[A-Z]{2}\\d{5}$")
                && plane.getBrand() != null && !plane.getBrand().isBlank()
                && plane.getModel() != null && !plane.getModel().isBlank()
                && plane.getAirline() != null && !plane.getAirline().isBlank()
                && plane.getMaxCapacity() > 0;
    }

    public static boolean isValidLocation(Location loc) {
        return loc.getAirportId().matches("^[A-Z]{3}$")
                && loc.getAirportName() != null && !loc.getAirportName().isBlank()
                && loc.getAirportCity() != null && !loc.getAirportCity().isBlank()
                && loc.getAirportCountry() != null && !loc.getAirportCountry().isBlank()
                && loc.getAirportLatitude() >= -90 && loc.getAirportLatitude() <= 90
                && loc.getAirportLongitude() >= -180 && loc.getAirportLongitude() <= 180;
    }

    public static boolean isValidFlight(Flight f) {
        return f.getId().matches("^[A-Z]{3}\\d{3}$")
                && f.getPlane() != null
                && f.getOrigin() != null
                && f.getDestination() != null
                && f.getDepartureTime() != null
                && f.getFlightTime() != null && !f.getFlightTime().isZero()
                && f.getOrigin().getAirportId() != null
                && f.getDestination().getAirportId() != null
                && (f.getScale() == null || f.getScaleTime() != null);
    }

    public static boolean isValidFlightDelay(Flight f, int hours, int minutes) {
        return f != null
                && (hours > 0 || minutes > 0);
    }
}
