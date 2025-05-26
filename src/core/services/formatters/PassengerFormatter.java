package core.services.formatters;

import core.models.Passenger;
import core.services.AgeCalculator;

/**
 * Formats a Passenger object into an array of Strings for display purposes.
 * Includes age calculation and phone number formatting.
 */
public class PassengerFormatter implements Formatter<Passenger> {

    @Override
    public String[] format(Passenger passenger) {
        int passengerAge = AgeCalculator.calculateAge(passenger.getBirthDate());
        PhoneFormatter phoneFormatter = new PhoneFormatter();
        String fullPhone = phoneFormatter.format(passenger)[0];

        return new String[]{
            String.valueOf(passenger.getId()),
            passenger.getFullname(),
            passenger.getBirthDate().toString(),
            String.valueOf(passengerAge),
            fullPhone,
            passenger.getCountry(),
            String.valueOf(passenger.getNumFlights())
        };
    }
}