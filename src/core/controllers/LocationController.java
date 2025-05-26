/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.responses.Response;
import core.controllers.responses.Status;
import core.models.Location;
import core.models.storage.LocationStorage;
import core.models.storage.utils.LocationDataLoader;
import core.models.storage.utils.LineFileReader;
import core.services.LocationOrderer;
import core.services.servicesFormatters.LocationFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
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
            validateLocationId(id);
            if (LocationStorage.getInstance().get(id) != null) {
                throw new IllegalArgumentException("Ya existe una ubicación con ese ID.");
            }

            validateStringField(name, "El nombre");
            validateStringField(city, "La ciudad");
            validateStringField(country, "El país");

            double latitude = parseCoordinate(latitudeStr, "latitud", -90, 90);
            double longitude = parseCoordinate(longitudeStr, "longitud", -180, 180);

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

    // --- Métodos de validación y parseo auxiliares ---
    private static void validateLocationId(String id) {
        if (id == null || id.length() != 3) {
            throw new IllegalArgumentException("El ID debe tener exactamente 3 caracteres.");
        }
        if (!id.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("El ID solo puede contener letras mayúsculas.");
        }
    }

    private static void validateStringField(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío.");
        }
    }

    private static double parseCoordinate(String coordStr, String coordName, double min, double max) {
        try {
            double coord = Double.parseDouble(coordStr);
            if (coord < min || coord > max) {
                throw new IllegalArgumentException("La " + coordName + " debe estar entre " + min + " y " + max + ".");
            }
            return coord;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La " + coordName + " debe ser un número válido.");
        }
    }
}