/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.patterns.observer;

import core.controllers.PlaneController;
import core.controllers.responses.Response;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Observer for updating a JTable with plane data.
 * @author User
 */
public class PlaneTableObserver extends Observer {

    private DefaultTableModel tableModel;

    public PlaneTableObserver(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }

    @Override
    public void notify(int value) {
        updateTableContent(); // Delegate to a private method for clarity
    }

    /**
     * Clears the table and reloads it with formatted plane data.
     */
    private void updateTableContent() {
        tableModel.setRowCount(0); // Clear existing rows
        Response response = PlaneController.getPlanesWithFormat();
        handleResponse(response, tableModel); // Centralized error handling
    }

    /**
     * Handles the response from a controller, displaying messages or updating the table.
     * @param response The Response object from the controller.
     * @param model The DefaultTableModel to update.
     */
    private void handleResponse(Response response, DefaultTableModel model) {
        if (response.getStatus() >= 500) {
            JOptionPane.showMessageDialog(null, response.getMessage(), "Error " + response.getStatus(), JOptionPane.ERROR_MESSAGE);
        } else if (response.getStatus() >= 400) {
            JOptionPane.showMessageDialog(null, response.getMessage(), "Error " + response.getStatus(), JOptionPane.WARNING_MESSAGE);
        } else {
            // Ensure safe casting and handle potential null object
            if (response.getObject() instanceof ArrayList) {
                for (String[] data : (ArrayList<String[]>) response.getObject()) {
                    model.addRow(data);
                }
            }
        }
    }
}