package core.controllers;

import core.controllers.responses.Response;
import core.controllers.responses.Status;
import core.models.Plane;
import core.models.storage.PlaneStorage;
import core.models.storage.utils.PlaneDataLoader;
import core.models.storage.utils.LineFileReader;
import core.services.PlaneOrderer;
import core.services.formatters.PlaneFormatter;
import core.controllers.validators.PlaneValidator; // Import the new validator
import java.util.ArrayList;
import java.util.List;

public class PlaneController {

    public static Response loadPlanesFromJson(String path) {
        try {
            PlaneDataLoader loader = new PlaneDataLoader(PlaneStorage.getInstance());
            String jsonPlanes = LineFileReader.readFile(path);
            loader.loadFromFile(jsonPlanes);
            return new Response("Aviones cargados exitosamente.", Status.OK);
        } catch (Exception e) {
            return new Response("No se pudieron cargar los aviones. Intente de nuevo más tarde.", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response getAllPlanes() {
        ArrayList<Plane> copiaList = new ArrayList<>();
        try {
            ArrayList<Plane> originalList = PlaneStorage.getInstance().getAll();
            copiaList = PlaneOrderer.orderPlanes(originalList);
        } catch (Exception e) {
            return new Response("No se pudieron recuperar los aviones. Intente de nuevo más tarde.", Status.INTERNAL_SERVER_ERROR, new ArrayList<>());
        }
        return new Response("Aviones recuperados exitosamente.", Status.OK, copiaList);
    }

    public static Response addPlane(String id, String brand, String model, String maxCapacity, String airline) {
        try {
            PlaneValidator.validatePlaneId(id); // Use validator
            if (PlaneStorage.getInstance().get(id) != null) {
                throw new IllegalArgumentException("Ya existe un avión con ese ID.");
            }

            PlaneValidator.validateStringField(brand, "La marca"); // Use validator
            PlaneValidator.validateStringField(model, "El modelo"); // Use validator
            PlaneValidator.validateStringField(airline, "La aerolínea"); // Use validator

            int intMaxCapacity = PlaneValidator.parseMaxCapacity(maxCapacity); // Use validator

            PlaneStorage.getInstance().add(new Plane(id, brand, model, intMaxCapacity, airline));
            return new Response("Avión creado exitosamente.", Status.CREATED);
        } catch (IllegalArgumentException e) {
            return new Response(e.getMessage(), Status.BAD_REQUEST);
        } catch (Exception e) {
            return new Response("Ocurrió un error inesperado al agregar el avión. Por favor, intente de nuevo.", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response getPlanesWithFormat() {
        try {
            List<Plane> planes = (List<Plane>) getAllPlanes().getObject();
            PlaneFormatter formatter = new PlaneFormatter();
            ArrayList<String[]> data = new ArrayList<>();
            planes.forEach(plane -> data.add(formatter.format(plane)));
            return new Response("Aviones recuperados exitosamente.", Status.OK, data);
        } catch (Exception e) {
            return new Response("No se pudieron recuperar los aviones con formato. Intente de nuevo más tarde.", Status.INTERNAL_SERVER_ERROR, new ArrayList<>());
        }
    }
}