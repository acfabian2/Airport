package core.services;

import core.models.Flight;

/**
 * Coordinates actions related to flight management, such as delaying flights.
 */
public class FlightCoordinator {

    /**
     * Delays a given flight by the specified number of hours and minutes.
     * This updates the flight's departure date.
     *
     * @param flight The Flight object to delay.
     * @param hours The number of hours to delay.
     * @param minutes The number of minutes to delay.
     */
    public void delay(Flight flight, int hours, int minutes) {
        // LocalDateTime's plusHours and plusMinutes methods are immutable,
        // returning a new LocalDateTime object with the adjusted time.
        flight.setDepartureDate(flight.getDepartureDate().plusHours(hours).plusMinutes(minutes));
    }
}