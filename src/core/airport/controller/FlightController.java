package core.airport.controller;

import core.airport.model.Flight;
import core.airport.model.Passenger;
import core.airport.model.Plane;
import core.airport.model.Location;
import core.airport.model.storage.FlightStorage;
import core.airport.model.storage.PlaneStorage;
import core.airport.model.storage.LocationStorage;
import core.airport.model.storage.PassengerStorage;
import core.airport.response.Response;
import core.airport.util.Validator;

import java.time.Duration;
import java.time.LocalDateTime;

public class FlightController {

    public Response registerFlight(Flight f) {
        if (!Validator.isValidFlight(f)) {
            return new Response(400, "Datos inválidos del vuelo");
        }

        if (FlightStorage.getFlightById(f.getId()) != null) {
            return new Response(409, "Ya existe un vuelo con ID " + f.getId());
        }

        if (PlaneStorage.getPlaneById(f.getPlane().getId()) == null) {
            return new Response(400, "El avión del vuelo no existe");
        }

        if (LocationStorage.getLocationById(f.getOrigin().getAirportId()) == null ||
            LocationStorage.getLocationById(f.getDestination().getAirportId()) == null) {
            return new Response(400, "La localización de origen o destino no existe");
        }

        if (f.getScale() != null &&
            LocationStorage.getLocationById(f.getScale().getAirportId()) == null) {
            return new Response(400, "La localización de escala no existe");
        }

        boolean added = FlightStorage.addFlight(f);
        if (!added) {
            return new Response(409, "Error al registrar el vuelo");
        }

        return new Response(200, "Vuelo registrado exitosamente", new Flight(f));
    }

    public Response delayFlight(String id, int hours, int minutes) {
        Flight f = FlightStorage.getFlightById(id);
        if (f == null) {
            return new Response(404, "Vuelo no encontrado");
        }

        if (!Validator.isValidFlightDelay(f, hours, minutes)) {
            return new Response(400, "Tiempo de retraso inválido");
        }

        LocalDateTime nuevaSalida = f.getDepartureTime().plusHours(hours).plusMinutes(minutes);
        f.setDepartureTime(nuevaSalida);
        return new Response(200, "Vuelo retrasado exitosamente", new Flight(f));
    }

    public Response addPassengerToFlight(String flightId, Passenger p) {
        Flight f = FlightStorage.getFlightById(flightId);
        if (f == null) {
            return new Response(404, "Vuelo no encontrado");
        }

        Passenger existente = PassengerStorage.getPassengerById(p.getId());
        if (existente == null) {
            return new Response(404, "Pasajero no registrado en el sistema");
        }

        if (f.getPassengers().stream().anyMatch(pass -> pass.getId() == p.getId())) {
            return new Response(409, "El pasajero ya está en el vuelo");
        }

        if (f.getNumPassengers() >= f.getPlane().getMaxCapacity()) {
            return new Response(400, "Capacidad máxima del vuelo alcanzada");
        }

        f.addPassenger(p);
        return new Response(200, "Pasajero añadido al vuelo", new Flight(f));
    }

    public Response getAllFlightsSorted() {
        return new Response(200, "Lista de vuelos obtenida", FlightStorage.getAllSorted());
    }

    public Response getFlightsByPassengerId(long passengerId) {
        return new Response(200, "Vuelos del pasajero obtenidos", FlightStorage.getFlightsByPassengerId(passengerId));
    }
}

