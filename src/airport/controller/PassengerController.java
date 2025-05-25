package airport.controller;

import airport.model.Passenger;
import airport.model.storage.PassengerStorage;
import airport.response.Response;
import airport.util.Validator;

public class PassengerController {

    public Response registerPassenger(Passenger p) {
        if (!Validator.isValidPassenger(p)) {
            return new Response(400, "Datos inválidos del pasajero");
        }

        boolean added = PassengerStorage.addPassenger(p);
        if (!added) {
            return new Response(409, "El pasajero con ID " + p.getId() + " ya existe");
        }

        return new Response(200, "Pasajero registrado exitosamente", new Passenger(p));
    }

    public Response updatePassenger(Passenger p) {
        if (!Validator.isValidPassenger(p)) {
            return new Response(400, "Datos inválidos del pasajero");
        }

        boolean updated = PassengerStorage.updatePassenger(p);
        if (!updated) {
            return new Response(404, "No se encontró un pasajero con ID " + p.getId());
        }

        return new Response(200, "Pasajero actualizado exitosamente", new Passenger(p));
    }

    public Response getPassengerById(long id) {
        Passenger found = PassengerStorage.getPassengerById(id);
        if (found == null) {
            return new Response(404, "Pasajero no encontrado");
        }
        return new Response(200, "Pasajero encontrado", found);
    }

    public Response getAllPassengersSorted() {
        return new Response(200, "Lista de pasajeros obtenida correctamente", PassengerStorage.getAllSorted());
    }
}
