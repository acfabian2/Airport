package core.controller;

import core.model.Location;
import core.model.storage.LocationStorage;
import core.response.Response;
import core.util.Validator;

public class LocationController {

    public Response registerLocation(Location loc) {
        if (!Validator.isValidLocation(loc)) {
            return new Response(400, "Datos inválidos de la localización");
        }

        boolean added = LocationStorage.addLocation(loc);
        if (!added) {
            return new Response(409, "Ya existe una localización con ID " + loc.getAirportId());
        }

        return new Response(200, "Localización registrada exitosamente", new Location(loc));
    }

    public Response getLocationById(String id) {
        Location found = LocationStorage.getLocationById(id);
        if (found == null) {
            return new Response(404, "Localización no encontrada");
        }
        return new Response(200, "Localización encontrada", found);
    }

    public Response getAllLocationsSorted() {
        return new Response(200, "Lista de localizaciones obtenida correctamente", LocationStorage.getAllSorted());
    }
}
