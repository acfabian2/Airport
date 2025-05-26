/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.patterns.observer;

import core.controllers.PassengerController;
import core.controllers.responses.Response;
import core.models.Passenger;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 * Observer for updating a JComboBox with passenger IDs.
 *
 * @author User
 */
public class PassengerComboBoxObserver extends Observer {

    private JComboBox comboBox1;

    // Define a constant for the notification value
    public static final int PASSENGER_ADDED = 1;

    public PassengerComboBoxObserver(JComboBox comboBox1) {
        this.comboBox1 = comboBox1;
        initializeComboBox();
    }

    private void initializeComboBox() {
        comboBox1.addItem("Select User"); // Add initial item
    }

    @Override
    public void notify(int value) {
        if (value == PASSENGER_ADDED) {
            comboBox1.removeAllItems(); // Clear existing items
            initializeComboBox(); // Re-add the default item

            Response response = PassengerController.getAllPassengers();
            handleResponse(response, comboBox1);
        }
    }

    // Centralized response handling
    private void handleResponse(Response response, JComboBox comboBox) {
        if (response.getStatus() >= 500) {
            JOptionPane.showMessageDialog(null, response.getMessage(), "Error " + response.getStatus(), JOptionPane.ERROR_MESSAGE);
        } else if (response.getStatus() >= 400) {
            JOptionPane.showMessageDialog(null, response.getMessage(), "Error " + response.getStatus(), JOptionPane.WARNING_MESSAGE);
        } else {
            // Ensure proper casting and null check if response.getObject() could be null
            if (response.getObject() instanceof ArrayList) {
                for (Passenger p : (ArrayList<Passenger>) response.getObject()) {
                    comboBox.addItem(String.valueOf(p.getId())); // Use String.valueOf for clarity
                }
            }
        }
    }
}