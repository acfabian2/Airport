/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.storage.utils;

import core.models.Flight; //
import core.models.Location; //
import core.models.Plane; //
import core.models.storage.FlightStorage; //
import core.models.storage.LocationStorage; //
import core.models.storage.PlaneStorage; //
import java.time.LocalDateTime; //
import org.json.JSONArray; //
import org.json.JSONObject; //
import java.io.IOException; //

/**
 *
 * @author User
 */
public class FlightDataLoader implements JsonDataLoader<Flight> { //

    private final FlightStorage flights; //
    private final PlaneStorage planes; //
    private final LocationStorage locations; //

    public FlightDataLoader(FlightStorage flights, PlaneStorage planes, LocationStorage locations) { //
        this.flights = flights; //
        this.planes = planes; //
        this.locations = locations; //
    }

    @Override
    public void loadFromFile(String jsonStr) throws IOException { //
        JSONArray jsonArray = new JSONArray(jsonStr); //
        for (int i = 0; i < jsonArray.length(); i++) { //
            JSONObject flightJson = jsonArray.getJSONObject(i); //

            try {
                String id = flightJson.getString("id"); //

                String planeId = flightJson.getString("plane"); //
                // Considerar si get() puede devolver null y cómo manejarlo.
                // Podrías lanzar una excepción o registrar un error si no se encuentra.
                Plane plane = this.planes.get(planeId); //
                if (plane == null) {
                    System.err.println("Advertencia: Avión con ID " + planeId + " no encontrado para el vuelo " + id);
                    continue; // Saltar este vuelo si el avión no existe
                }

                String departureLocationId = flightJson.getString("departureLocation"); //
                Location departureLocation = this.locations.get(departureLocationId); //
                if (departureLocation == null) {
                    System.err.println("Advertencia: Ubicación de partida " + departureLocationId + " no encontrada para el vuelo " + id);
                    continue;
                }

                String arrivalLocationId = flightJson.getString("arrivalLocation"); //
                Location arrivalLocation = this.locations.get(arrivalLocationId); //
                if (arrivalLocation == null) {
                    System.err.println("Advertencia: Ubicación de llegada " + arrivalLocationId + " no encontrada para el vuelo " + id);
                    continue;
                }

                String departureDateString = flightJson.getString("departureDate"); //
                LocalDateTime departureDate = LocalDateTime.parse(departureDateString); //

                int hoursDurationArrival = flightJson.getInt("hoursDurationArrival"); //
                int minutesDurationArrival = flightJson.getInt("minutesDurationArrival"); //
                int hoursDurationScale = flightJson.optInt("hoursDurationScale", 0); // Usar optInt con valor por defecto
                int minutesDurationScale = flightJson.optInt("minutesDurationScale", 0); // Usar optInt con valor por defecto

                Flight newFlight;

                // Uso de optString para manejar la ausencia de "scaleLocation" de forma más limpia
                String scaleLocationId = flightJson.optString("scaleLocation", null); //

                if (scaleLocationId == null || scaleLocationId.isEmpty() || flightJson.isNull("scaleLocation")) { //
                    newFlight = new Flight(
                            id, plane, departureLocation, arrivalLocation,
                            departureDate, hoursDurationArrival, minutesDurationArrival
                    );
                } else {
                    Location scaleLocation = this.locations.get(scaleLocationId); //
                    if (scaleLocation == null) {
                        System.err.println("Advertencia: Ubicación de escala " + scaleLocationId + " no encontrada para el vuelo " + id);
                        continue;
                    }
                    newFlight = new Flight(
                            id, plane, departureLocation, scaleLocation, arrivalLocation,
                            departureDate, hoursDurationArrival, minutesDurationArrival,
                            hoursDurationScale, minutesDurationScale
                    );
                }

                this.flights.add(newFlight); //

            } catch (Exception e) {
                // Manejar errores de parseo o datos faltantes para un vuelo específico
                System.err.println("Error al cargar el vuelo: " + flightJson.toString() + " - " + e.getMessage());
                // Podrías lanzar una excepción personalizada aquí si prefieres un fallo total
                // throw new RuntimeException("Error processing flight JSON", e);
            }
        }
    }
}