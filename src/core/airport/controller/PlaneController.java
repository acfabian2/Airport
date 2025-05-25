package core.airport.controller;

import core.airport.model.Plane;
import core.airport.model.storage.PlaneStorage;
import core.airport.response.Response;
import core.airport.util.Validator;

public class PlaneController {

    public Response registerPlane(Plane plane) {
        if (!Validator.isValidPlane(plane)) {
            return new Response(400, "Datos inválidos del avión");
        }

        boolean added = PlaneStorage.addPlane(plane);
        if (!added) {
            return new Response(409, "Ya existe un avión con ID " + plane.getId());
        }

        return new Response(200, "Avión registrado exitosamente", new Plane(plane));
    }

    public Response getPlaneById(String id) {
        Plane found = PlaneStorage.getPlaneById(id);
        if (found == null) {
            return new Response(404, "Avión no encontrado");
        }
        return new Response(200, "Avión encontrado", found);
    }
    
    public Response getAllPlanesSorted() {
        return new Response(200, "Lista de aviones obtenida correctamente", PlaneStorage.getAllSorted());
    }
}
