package core.services.formatters;

import core.models.Plane;

/**
 * Formats a Plane object into an array of Strings for display purposes.
 */
public class PlaneFormatter implements Formatter<Plane> {

    @Override
    public String[] format(Plane plane) {
        return new String[]{
            plane.getId(),
            plane.getBrand(),
            plane.getModel(),
            String.valueOf(plane.getMaxCapacity()),
            plane.getAirline(),
            String.valueOf(plane.getNumFlights())
        };
    }
}