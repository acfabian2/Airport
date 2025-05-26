/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.patterns.observer;

import core.controllers.PassengerController;
import core.controllers.responses.Response;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Observer for updating a JTable with all passengers.
 *
 * @author User
 */
public class PassengerTableObserver extends Observer {

    private DefaultTableModel tableModel;

    public PassengerTableObserver(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }

    @Override
    public void notify(int value) {
        updatePassengerTable();
    }

    private void updatePassengerTable() {
        tableModel.setRowCount(0); // Clear existing rows
        Response response = PassengerController.getPassengersWithFormat();
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