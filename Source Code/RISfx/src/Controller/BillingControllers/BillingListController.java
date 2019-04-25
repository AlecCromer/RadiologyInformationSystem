package Controller.BillingControllers;

import Controller.Main;
import Model.Appointment;
import Model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class BillingListController implements Initializable {

    @FXML
    TableView<Appointment> BillingList;
    @FXML
    TableColumn<Appointment, Integer> patientID;
    @FXML
    TableColumn<Appointment, String> fName, address, patientStatus, Balance;
    @FXML
    private TextField searchField;

    /**
     * Sets center pane to BillingList.fxml
     * @throws Exception
     */
    public static void setView() throws Exception {
        Main.setCenterPane("BillingViews/BillingList.fxml");
    }

    @SuppressWarnings("Duplicates")

    /**
     * populates table via updateTable()
     * has onclick method that passes object you click on to invoice controller so you can send a bill.
     */
    public void initialize(URL url, ResourceBundle arg1) {
        try {
            updateTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BillingList.setOnMouseClicked((MouseEvent event) -> {
            //DOUBLE CLICK ON CELL
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                try {
                    Main.setAppointmentFocus(BillingList.getSelectionModel().getSelectedItem());
                    InvoiceController.setView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * runs getBillingList() to get list of appointment objects
     * uses attributes of those billing objects to set FXML values row by row
     */
    private void updateTable() throws Exception {
        patientID.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("appointmentId"));
        fName.setCellValueFactory(new PropertyValueFactory<Appointment, String>("patientFullName"));
        address.setCellValueFactory(new PropertyValueFactory<Appointment, String>("address"));
        patientStatus.setCellValueFactory(new PropertyValueFactory<Appointment, String>("patientStatus"));
        Balance.setCellValueFactory(new PropertyValueFactory<Appointment, String>("balance"));

        FilteredList<Appointment> sortedBilling = new FilteredList<>(Appointment.getBillingList(), p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            sortedBilling.setPredicate(appointment -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searched = newValue.toLowerCase();

                if (appointment.getPatientFullName().toLowerCase().contains(searched)) {
                    return true;
                } else if (Float.toString(appointment.getBalance()).contains(searched)) {
                    return true;
                } else if (appointment.getPatientStatus().toLowerCase().contains(searched)) {
                    return true;
                } else if (appointment.getAddress().toLowerCase().contains(searched)) {
                    return true;
                } else if (Integer.toString(appointment.getAppointmentId()).contains(searched)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<Appointment> sortedData = new SortedList<>(sortedBilling);

        sortedData.comparatorProperty().bind(BillingList.comparatorProperty());

        BillingList.setItems(sortedBilling);
    }
}

