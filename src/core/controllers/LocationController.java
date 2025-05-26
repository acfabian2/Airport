package core.controllers;

import core.controllers.responses.Response;
import core.controllers.responses.Status;
import core.models.Location;
import core.models.storage.LocationStorage;
import core.models.storage.utils.LocationDataLoader;
import core.models.storage.utils.LineFileReader;
import core.services.LocationOrderer;
import core.services.formatters.LocationFormatter;
import core.controllers.validators.LocationValidator; // Import the new validator
import java.util.ArrayList;
import java.util.List;

public class LocationController {

    public static Response loadLocationsFromJson(String path) {
        try {
            LocationDataLoader loader = new LocationDataLoader(LocationStorage.getInstance());
            String jsonLocations = LineFileReader.readFile(path);
            loader.loadFromFile(jsonLocations);
            return new Response("Ubicaciones cargadas exitosamente.", Status.OK);
        } catch (Exception e) {
            return new Response("No se pudieron cargar las ubicaciones. Intente de nuevo más tarde.", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response getAllLocations() {
        try {
            ArrayList<Location> originalList = LocationStorage.getInstance().getAll();
            ArrayList<Location> orderedList = LocationOrderer.order(originalList);
            return new Response("Ubicaciones recuperadas exitosamente.", Status.OK, orderedList);
        } catch (Exception e) {
            return new Response("No se pudieron recuperar las ubicaciones. Intente de nuevo más tarde.", Status.INTERNAL_SERVER_ERROR, new ArrayList<>());
        }
    }

    public static Response addLocation(String id, String name, String city, String country, String longitudeStr, String latitudeStr) {
        try {
            LocationValidator.validateLocationId(id); // Use validator
            if (LocationStorage.getInstance().get(id) != null) {
                throw new IllegalArgumentException("Ya existe una ubicación con ese ID.");
            }

            LocationValidator.validateStringField(name, "El nombre"); // Use validator
            LocationValidator.validateStringField(city, "La ciudad"); // Use validator
            LocationValidator.validateStringField(country, "El país"); // Use validator

            double latitude = LocationValidator.parseCoordinate(latitudeStr, "latitud", -90, 90); // Use validator
            double longitude = LocationValidator.parseCoordinate(longitudeStr, "longitud", -180, 180); // Use validator

            LocationStorage.getInstance().add(new Location(id, name, city, country, longitude, latitude));
            return new Response("Ubicación agregada exitosamente.", Status.CREATED);
        } catch (IllegalArgumentException e) {
            return new Response(e.getMessage(), Status.BAD_REQUEST);
        } catch (Exception e) {
            return new Response("Ocurrió un error inesperado al agregar la ubicación. Por favor, intente de nuevo.", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response getLocationsWithFormat() {
        try {
            List<Location> locations = (List<Location>) getAllLocations().getObject();
            LocationFormatter formatter = new LocationFormatter();
            ArrayList<String[]> data = new ArrayList<>();
            locations.forEach(location -> data.add(formatter.format(location)));
            return new Response("Ubicaciones recuperadas exitosamente.", Status.OK, data);
        } catch (Exception e) {
            return new Response("No se pudieron recuperar las ubicaciones con formato. Intente de nuevo más tarde.", Status.INTERNAL_SERVER_ERROR, new ArrayList<>());
        }
    }
}