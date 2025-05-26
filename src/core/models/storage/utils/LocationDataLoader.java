/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.storage.utils;

import core.models.Location; //
import core.models.storage.LocationStorage; //
import org.json.JSONArray; //
import org.json.JSONObject; //
import java.io.IOException; //

/**
 *
 * @author User
 */
public class LocationDataLoader implements JsonDataLoader<Location>  { //
    
    private final LocationStorage locations; //

    public LocationDataLoader(LocationStorage locations) { //
        this.locations = locations; //
    }
    
    @Override
    public void loadFromFile(String jsonStr) throws IOException { //
        JSONArray jsonArray = new JSONArray(jsonStr); //
        for(int i = 0; i < jsonArray.length(); i++) { //
            JSONObject locationJson = jsonArray.getJSONObject(i); //
            
            try {
                String airportId = locationJson.getString("airportId"); //
                String airportName = locationJson.getString("airportName"); //
                String airportCity = locationJson.getString("airportCity"); //
                String airportCountry = locationJson.getString("airportCountry"); //
                
                double airportLatitude = locationJson.getDouble("airportLatitude"); //
                double airportLongitude = locationJson.getDouble("airportLongitude"); //
                
                Location newLocation = new Location(airportId,airportName,airportCity,airportCountry,airportLatitude,airportLongitude); //
                this.locations.add(newLocation); //
            } catch (Exception e) {
                System.err.println("Error al cargar la ubicación: " + locationJson.toString() + " - " + e.getMessage());
            }
        }
    }
}