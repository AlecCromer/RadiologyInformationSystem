package Controller.BillingControllers;

import Controller.AppointmentControllers.AppointmentListController;
import Controller.AppointmentControllers.AppointmentViewController;
import Controller.Main;
import Model.Appointment;
import javafx.beans.Observable;
import Model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

    @FXML TableView<Patient>        BillingList;
    @FXML
    TableColumn<Patient, Integer>   patientID;
    @FXML
    TableColumn<Patient, String>    fName, address, patientStatus, Balance;


    public static void setView() throws Exception{
        Main.setCenterPane("BillingViews/BillingList.fxml");
    }

    public void initialize(URL url, ResourceBundle arg1) {
        updateTable();
        BillingList.setOnMouseClicked((MouseEvent event) -> {
            //DOUBLE CLICK ON CELL
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                try{
                    //Main.setAppointmentFocus(BillingList.getSelectionModel().getSelectedItem());
                    //sendAppointmentToView(AppointmentList.getSelectionModel().getSelectedItem());
                    //AppointmentViewController.setView();
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
        patientID.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("patientID"));
        fName.setCellValueFactory(new PropertyValueFactory<Patient, String>("fullName"));
        address.setCellValueFactory(new PropertyValueFactory<Patient, String>("address"));
        patientStatus.setCellValueFactory(new PropertyValueFactory<Patient, String>("patientStatus"));
        Balance.setCellValueFactory(new PropertyValueFactory<Patient, String>("balance"));
    }

    private ObservableList<Patient> getBillingList() throws Exception {
        ResultSet rs = Patient.queryInfoForBillingList();
        ObservableList<Patient> billingList = FXCollections.observableArrayList();

        while (rs.next()) {
            //int appointmentId, Date appointmentDate, String patientFullName, String patientStatus, float balance
            billingList.add(new Patient(
                    rs.getInt("patient_id"),
                    rs.getString("first_name") + " " + rs.getString("last_name"),
                    rs.getString("street_name") + " " + rs.getString("city") + ", " + rs.getString("state") + " " + rs.getString("zip"),
                    rs.getString("status"),
                    rs.getFloat("balance")
            ));
        }



        return billingList;
    }

    public void setBillView(ActionEvent actionEvent) {
    }
}
