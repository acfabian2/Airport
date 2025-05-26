package core.services.servicesFormatters;

import core.models.Flight;
import core.services.ArrivalCalculator;
import java.time.LocalDateTime;

/**
 * Formats a Flight object associated with a passenger into an array of Strings.
 * This typically includes flight ID, departure date, and calculated arrival date.
 */
public class PassengerFlightFormatter implements Formatter<Flight> {

    @Override
    public String[] format(Flight flight) {
        LocalDateTime arrivalDate = ArrivalCalculator.calculate(flight);
        return new String[]{
            flight.getId(),
            flight.getDepartureDate().toString(),
            arrivalDate.toString()
        };
    }
}