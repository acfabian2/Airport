/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.patterns.observer;

import core.controllers.PassengerController;
import core.controllers.responses.Response;
import core.models.Passenger;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Observer for updating a JTable with a passenger's flights.
 *
 * @author User
 */
public class PassengerFlightTableObserver extends Observer {

    private DefaultTableModel tableModel;
    private Passenger currentUser = null; // Initialize to null for safety

    // Define a constant for the notification value
    public static final int CURRENT_USER_UPDATED = 3;

    public PassengerFlightTableObserver(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }

    @Override
    public void notify(int value) {
        if (value == CURRENT_USER_UPDATED) {
            this.currentUser = UserManager.getInstance().getCurrentUser(); // Update current user
        }

        // Always update the table when notified, as current user might have changed, or data was updated
        updatePassengerFlightsTable();
    }

    private void updatePassengerFlightsTable() {
        tableModel.setRowCount(0); // Clear existing rows

        if (this.currentUser == null) {
            // Handle case where no user is selected or available
            // JOptionPane.showMessageDialog(null, "No passenger selected.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Response response = PassengerController.showPassengerFlights(String.valueOf(this.currentUser.getId()));
        handleResponse(response, tableModel);
    }

    // Centralized response handling for tables
    private void handleResponse(Response response, DefaultTableModel model) {
        if (response.getStatus() >= 500) {
            JOptionPane.showMessageDialog(null, response.getMessage(), "Error " + response.getStatus(), JOptionPane.ERROR_MESSAGE);
        } else if (response.getStatus() >= 400) {
            JOptionPane.showMessageDialog(null, response.getMessage(), "Error " + response.getStatus(), JOptionPane.WARNING_MESSAGE);
        } else {
            // Ensure proper casting and null check
            if (response.getObject() instanceof ArrayList) {
                for (String[] data : (ArrayList<String[]>) response.getObject()) {
                    model.addRow(data);
                }
            }
        }
    }
}