package Controller.BillingControllers;

import Controller.Main;
import Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class BillingListController implements Initializable {

    @FXML TableView<Appointment>        BillingList;
    @FXML
    TableColumn<Appointment, Integer>   patientID;
    @FXML
    TableColumn<Appointment, String>    fName, address, patientStatus, Balance;

    public static void setView() throws Exception{
        Main.setCenterPane("BillingViews/BillingList.fxml");
    }
    @SuppressWarnings("Duplicates")
    public void initialize(URL url, ResourceBundle arg1) {
        updateTable();
        BillingList.setOnMouseClicked((MouseEvent event) -> {
            //DOUBLE CLICK ON CELL
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                try{
                    Main.setAppointmentFocus(BillingList.getSelectionModel().getSelectedItem());
                    InvoiceController.setView();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateTable() {
        try {
            BillingList.setItems(getBillingList());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("UNABLE TO FILL TABLE");
            e.printStackTrace();
        }
        patientID.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("appointmentId"));
        fName.setCellValueFactory(new PropertyValueFactory<Appointment, String>("patientFullName"));
        address.setCellValueFactory(new PropertyValueFactory<Appointment, String>("address"));
        patientStatus.setCellValueFactory(new PropertyValueFactory<Appointment, String>("patientStatus"));
        Balance.setCellValueFactory(new PropertyValueFactory<Appointment, String>("balance"));
    }
    @SuppressWarnings("Duplicates")
    private ObservableList<Appointment> getBillingList() throws Exception {
        ResultSet rs = Appointment.queryForBillingAppointments();
        ObservableList<Appointment> billingList = FXCollections.observableArrayList();

        while (rs.next()) {
            //int appointmentId, int patientId, String patientFullName, String patientStatus, String[] address, float balance
            Appointment addition = new Appointment(
                    rs.getInt("appointment_id"),
                    rs.getInt("patient_id"),
                    rs.getInt("procedure_id"),
                    rs.getString("procedure_name"),
                    rs.getString("full_name"),
                    rs.getString("patient_status"),
                    rs.getDate("appointment_date")
            );
            addition.setAddress();
            addition.setBalance();
            billingList.add(addition);
        }

        return billingList;
    }
}
