package core.patterns.observer;

import core.controllers.FlightController;
import core.controllers.responses.Response;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FlightTableObserver extends Observer {

    private DefaultTableModel tableModel;

    public FlightTableObserver(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }

    @Override
    public void notify(int value) {
        updateTable(); // Delegate to a more descriptive method
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        Response response = FlightController.getFlightsWithFormat();
        handleResponse(response);
    }

    private void handleResponse(Response response) {
        if (response.getStatus() >= 500) {
            JOptionPane.showMessageDialog(null, response.getMessage(), "Error " + response.getStatus(), JOptionPane.ERROR_MESSAGE);
        } else if (response.getStatus() >= 400) {
            JOptionPane.showMessageDialog(null, response.getMessage(), "Error " + response.getStatus(), JOptionPane.WARNING_MESSAGE);
        } else {
            for (String[] data : (ArrayList<String[]>) response.getObject()) {
                tableModel.addRow(data);
            }
        }
    }
}