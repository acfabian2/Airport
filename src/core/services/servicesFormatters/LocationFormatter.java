package core.services.servicesFormatters;

import core.models.Location;

/**
 * Formats a Location object into an array of Strings for display purposes.
 */
public class LocationFormatter implements Formatter<Location> {

    @Override
    public String[] format(Location location) {
        return new String[]{
            location.getAirportId(),
            location.getAirportName(),
            location.getAirportCity(),
            location.getAirportCountry()
        };
    }
}