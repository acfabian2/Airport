/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.patterns.observer;

import core.controllers.PlaneController;
import core.controllers.responses.Response;
import core.models.Plane;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 * Observer for updating a JComboBox with plane IDs.
 *
 * @author User
 */
public class PlaneComboBoxObserver extends Observer {

    private JComboBox comboBox1;

    public PlaneComboBoxObserver(JComboBox comboBox1) {
        this.comboBox1 = comboBox1;
        initializeComboBox();
    }

    private void initializeComboBox() {
        comboBox1.addItem("Plane"); // Add initial item
    }

    @Override
    public void notify(int value) {
        // Assuming 'value' might indicate different types of updates,
        // but for now, it always reloads. If 'value' has specific meanings,
        // use constants and an 'if' block similar to PassengerComboBoxObserver.
        comboBox1.removeAllItems(); // Clear existing items
        initializeComboBox(); // Re-add the default item

        Response response = PlaneController.getAllPlanes();
        handleResponse(response, comboBox1);
    }

    // Centralized response handling for combo boxes
    private void handleResponse(Response response, JComboBox comboBox) {
        if (response.getStatus() >= 500) {
            JOptionPane.showMessageDialog(null, response.getMessage(), "Error " + response.getStatus(), JOptionPane.ERROR_MESSAGE);
        } else if (response.getStatus() >= 400) {
            JOptionPane.showMessageDialog(null, response.getMessage(), "Error " + response.getStatus(), JOptionPane.WARNING_MESSAGE);
        } else {
            // Ensure proper casting and null check
            if (response.getObject() instanceof ArrayList) {
                for (Plane p : (ArrayList<Plane>) response.getObject()) {
                    comboBox.addItem(p.getId()); // Add plane IDs
                }
            }
        }
    }
}