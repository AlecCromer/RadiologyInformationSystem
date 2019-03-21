package Controller.BillingControllers;

import Controller.AppointmentControllers.AppointmentViewController;
import Controller.Main;
import Model.Appointment;
import javafx.beans.Observable;
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

    @FXML TableView<Appointment>        BillingList;
    @FXML
    TableColumn<Appointment, Integer>   appointmentID;
    @FXML
    TableColumn<Appointment, String>    fName, dateField, patientStatus, Balance;


    public static void setView() throws Exception{
        Main.setCenterPane("BillingViews/BillingList.fxml");
    }

    public void initialize(URL url, ResourceBundle arg1) {
        updateTable();
        BillingList.setOnMouseClicked((MouseEvent event) -> {
            //DOUBLE CLICK ON CELL
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                try{
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
        appointmentID.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("appointmentId"));
        fName.setCellValueFactory(new PropertyValueFactory<Appointment, String>("patientFullName"));
        dateField.setCellValueFactory(new PropertyValueFactory<Appointment, String>("appointmentDate"));
        patientStatus.setCellValueFactory(new PropertyValueFactory<Appointment, String>("patientStatus"));
        Balance.setCellValueFactory(new PropertyValueFactory<Appointment, String>("balance"));
    }

    private ObservableList<Appointment> getBillingList() throws Exception {
        ResultSet rs = Appointment.queryBillingList();
        ObservableList<Appointment> billingList = FXCollections.observableArrayList();

        while (rs.next()) {
            //int appointmentId, Date appointmentDate, String patientFullName, String patientStatus, float balance
            billingList.add(new Appointment(
                rs.getInt("appointment_id"),
                rs.getDate("appointment_date"),
                rs.getString("first_name") + " " + rs.getString("last_name"),
                rs.getString("patient_status"),
                rs.getFloat("appointment_total")
            ));
        }



        return billingList;
    }

    public void setBillView(ActionEvent actionEvent) {
    }
}
