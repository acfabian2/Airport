package core.services;

import core.models.Flight;
import java.time.LocalDateTime;

/**
 * Provides a utility method to calculate the arrival time of a flight.
 */
public class ArrivalCalculator {

    /**
     * Calculates the estimated arrival time of a flight based on its departure date
     * and duration components (scale and arrival).
     *
     * @param flight The Flight object containing departure and duration details.
     * @return The calculated arrival time as a LocalDateTime.
     */
    public static LocalDateTime calculate(Flight flight) {
        // Summing all duration components first to potentially simplify,
        // then applying them in one go, or chaining directly as shown.
        // Chaining is often more readable for a few operations.
        return flight.getDepartureDate()
                .plusHours(flight.getHoursDurationScale())
                .plusHours(flight.getHoursDurationArrival())
                .plusMinutes(flight.getMinutesDurationScale())
                .plusMinutes(flight.getMinutesDurationArrival());
    }
}