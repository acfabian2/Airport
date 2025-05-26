/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.storage.utils;

import core.models.Plane; //
import core.models.storage.PlaneStorage; //
import org.json.JSONArray; //
import org.json.JSONObject; //
import java.io.IOException; //

/**
 *
 * @author User
 */
public class PlaneDataLoader implements JsonDataLoader<Plane>{ //
    
    private final PlaneStorage planes; //

    public PlaneDataLoader(PlaneStorage planes) { //
        this.planes = planes; //
    }
    
    @Override
    public void loadFromFile(String jsonStr) throws IOException { //
        JSONArray jsonArray = new JSONArray(jsonStr); //
        for (int i = 0; i < jsonArray.length(); i++) { //
            JSONObject planeJson = jsonArray.getJSONObject(i); //
            
            try {
                String id = planeJson.getString("id"); //
                String brand = planeJson.getString("brand"); //
                String model = planeJson.getString("model"); //
                int maxCapacity = planeJson.getInt("maxCapacity"); //
                String airline = planeJson.getString("airline"); //
                
                Plane newPlane = new Plane(id,brand,model,maxCapacity,airline); //
                this.planes.add(newPlane); //
            } catch (Exception e) {
                System.err.println("Error al cargar el avión: " + planeJson.toString() + " - " + e.getMessage());
            }
        }
    }
    
}