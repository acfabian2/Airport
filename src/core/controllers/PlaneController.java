/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.responses.Response;
import core.controllers.responses.Status;
import core.models.Plane;
import core.models.storage.PlaneStorage;
import core.models.storage.utils.PlaneDataLoader;
import core.models.storage.utils.LineFileReader;
import core.services.PlaneOrderer;
import core.services.formatters.PlaneFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
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
            validatePlaneId(id);
            if (PlaneStorage.getInstance().get(id) != null) {
                throw new IllegalArgumentException("Ya existe un avión con ese ID.");
            }

            validateStringField(brand, "La marca");
            validateStringField(model, "El modelo");
            validateStringField(airline, "La aerolínea");

            int intMaxCapacity = parseMaxCapacity(maxCapacity);

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

    // --- Métodos de validación y parseo auxiliares ---
    private static void validatePlaneId(String id) {
        if (id.isEmpty()) {
            throw new IllegalArgumentException("El ID no puede estar vacío.");
        }
        if (id.length() != 7) {
            throw new IllegalArgumentException("ID inválido: debe tener exactamente 7 caracteres (2 letras seguidas de 5 números).");
        }

        String idLetters = id.substring(0, 2);
        String idNumbers = id.substring(2, 7);

        if (!idLetters.matches("[A-Z]{2}")) {
            throw new IllegalArgumentException("ID inválido: los primeros 2 caracteres deben ser letras mayúsculas.");
        }
        if (!idNumbers.matches("\\d{5}")) {
            throw new IllegalArgumentException("ID inválido: los últimos 5 caracteres deben ser números.");
        }
    }

    private static void validateStringField(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacía.");
        }
    }

    private static int parseMaxCapacity(String maxCapacityStr) {
        if (maxCapacityStr.isEmpty()) {
            throw new IllegalArgumentException("La capacidad máxima no puede estar vacía.");
        }
        try {
            return Integer.parseInt(maxCapacityStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La capacidad máxima debe ser numérica.");
        }
    }
}