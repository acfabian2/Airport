package core.services.servicesFormatters;

import core.models.Flight;
import core.services.ArrivalCalculator;
import java.time.LocalDateTime;

/**
 * Formats a Flight object into an array of Strings for display purposes.
 */
public class FlightFormatter implements Formatter<Flight> {

    @Override
    public String[] format(Flight flight) {
        LocalDateTime arrivalDate = ArrivalCalculator.calculate(flight);
        return new String[]{
            flight.getId(),
            flight.getDepartureLocation().getAirportId(),
            flight.getArrivalLocation().getAirportId(),
            flight.getScaleLocation() != null ? flight.getScaleLocation().getAirportId() : "-",
            flight.getDepartureDate().toString(),
            arrivalDate.toString(),
            flight.getPlane().getId(),
            String.valueOf(flight.getNumPassengers())
        };
    }
}