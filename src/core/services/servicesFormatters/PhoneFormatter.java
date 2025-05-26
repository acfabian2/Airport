package core.services.servicesFormatters;

import core.models.Passenger;

/**
 * Formats a Passenger's phone number, including the country code, into a single String.
 */
public class PhoneFormatter implements Formatter<Passenger> {

    @Override
    public String[] format(Passenger passenger) {
        String fullPhone = "+" + passenger.getCountryPhoneCode() + " " + passenger.getPhone();
        return new String[]{fullPhone};
    }
}