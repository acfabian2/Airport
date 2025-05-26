/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.storage.utils;

import core.models.Passenger; //
import core.models.storage.PassengerStorage; //
import java.time.LocalDate; //
import org.json.JSONArray; //
import org.json.JSONObject; //
import java.io.IOException; //

/**
 *
 * @author User
 */
public class PassengerDataLoader implements JsonDataLoader<Passenger>{ //

    private final PassengerStorage passengers; //

    public PassengerDataLoader(PassengerStorage passengers) { //
        this.passengers = passengers; //
    }
    
    @Override
    public void loadFromFile(String jsonStr) throws IOException { //
        JSONArray jsonArray = new JSONArray(jsonStr); //
        for (int i = 0; i < jsonArray.length(); i++) { //
            JSONObject passengerJson = jsonArray.getJSONObject(i); //
            
            try {
                long id = passengerJson.getLong("id"); //
                String firstname = passengerJson.getString("firstname"); //
                String lastname = passengerJson.getString("lastname"); //
                String birthDateString = passengerJson.getString("birthDate"); //
                LocalDate birthDate = LocalDate.parse(birthDateString); //
                int countryPhoneCode = passengerJson.getInt("countryPhoneCode"); //
                long phone = passengerJson.getLong("phone"); //
                String country = passengerJson.getString("country"); //
                
                Passenger newPassenger = new Passenger(id,firstname,lastname,birthDate,countryPhoneCode,phone,country); //
                this.passengers.add(newPassenger); //
            } catch (Exception e) {
                System.err.println("Error al cargar el pasajero: " + passengerJson.toString() + " - " + e.getMessage());
            }
        }
    }
    
}